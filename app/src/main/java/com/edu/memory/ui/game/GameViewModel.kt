package com.edu.memory.ui.game

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.CountDownTimer
import com.edu.memory.data.PhotosRepository
import com.edu.memory.data.Resource
import com.edu.memory.data.ScoresRepository
import com.edu.memory.data.Status
import com.edu.memory.data.flickrapi.PhotoSearchResponse
import com.edu.memory.data.flickrapi.getDownloadUrl
import com.edu.memory.extensions.downloadPhotos
import com.edu.memory.model.Card
import com.edu.memory.model.Difficulty
import com.edu.memory.model.Score
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

/**
 * Created by edu
 */
class GameViewModel
@Inject constructor(val difficulty: Difficulty,
                    val photosRepository: PhotosRepository,
                    val scoresRepository: ScoresRepository) : ViewModel() {

    val cards: MediatorLiveData<Resource<List<Card>>> = MediatorLiveData()
    val timeInSeconds: MutableLiveData<Long> = MutableLiveData()
    val pairFlipCount: MutableLiveData<Int> = MutableLiveData()
    val score: MutableLiveData<Score> = MutableLiveData()

    private val flippedCards: MutableList<Card> = mutableListOf()
    private var currentSelectedCard: Card? = null

    private lateinit var timer: CountDownTimer

    init {
        initGame()
        pairFlipCount.value = 0
    }

    /**
     * Called when a card is selected from the game. Depending if there is already another card
     * selected or not, it will trigger [onPairSelected]
     */
    fun onCardSelected(card: Card): LiveData<Pair<Card, Card>> {
        val result = MutableLiveData<Pair<Card, Card>>()

        if (currentSelectedCard == null) {
            currentSelectedCard = card
        } else {
            if (card != currentSelectedCard) {
                return onPairSelected(Pair(currentSelectedCard!!, card))
            }
        }
        return result
    }

    /**
     * @return true if the given card is selected, or has been already flipped
     */
    fun isCardFlipped(card: Card): Boolean {
        return card == currentSelectedCard || flippedCards.contains(card)
    }

    /**
     * Called when a pair of cards has been selected. It will check it both are from the same pair,
     * and if so, store them in a list. Otherwise it will trigger them to be flipped back again.
     * If all the cards have been flipped already, it will call [onGameFinished]
     */
    private fun onPairSelected(pair: Pair<Card, Card>): LiveData<Pair<Card, Card>> {
        val result = MutableLiveData<Pair<Card, Card>>()
        pairFlipCount.value = pairFlipCount.value?.plus(1)

        if (pair.first.pairNumber == pair.second.pairNumber) {
            flippedCards.add(pair.first)
            flippedCards.add(pair.second)
            result.value = null
        } else {
            Timer().schedule(HIDE_CARDS_DELAY) { result.postValue(pair) }
        }
        if (cards.value?.data?.size == flippedCards.size) {
            onGameFinished()
        }
        currentSelectedCard = null
        return result
    }

    private fun onGameFinished() {
        saveScore()
    }

    private fun saveScore() {
        val score = Score(difficulty, timeInSeconds.value!!, pairFlipCount.value!!)
        scoresRepository.saveScore(score)
        this.score.value = score
    }

    /**
     * Initializes the game, downloading a list of photos for the cards and initializing them.
     */
    private fun initGame() {
        val searchPhotosSource = photosRepository.searchPhotos("kittens")
        cards.addSource(searchPhotosSource) { photos ->
            when (photos?.status) {
                Status.ERROR -> cards.value = Resource.error("")
                Status.LOADING -> cards.value = Resource.loading()
                Status.SUCCESS -> {
                    cards.removeSource(searchPhotosSource)
                    downloadPhotos(photos.data)
                    initCards(photos.data)
                }
            }
        }
    }

    private fun initCards(photos: List<PhotoSearchResponse.PhotoObject>?) {
        val cards = mutableListOf<Card>()

        for (i in 0 until difficulty.pairsCount) {
            val photo = photos?.get(i)
            val cardA = Card(pairNumber = i, photoUrl = photo.getDownloadUrl())
            val cardB = Card(pairNumber = i, photoUrl = photo.getDownloadUrl())
            cards.add(cardA)
            cards.add(cardB)
        }
        cards.shuffle()
        startGame(cards)
    }

    private fun startGame(cards: List<Card>) {
        this.cards.value = Resource.success(cards)
        initTimer()
    }

    /**
     * Initialize the timer that will count the time of the game.
     */
    private fun initTimer() {
        timer = object : CountDownTimer(MAX_TIME_MILLIS, 1000) {
            override fun onFinish() {}
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (MAX_TIME_MILLIS - millisUntilFinished) / 1000
                timeInSeconds.value = seconds
            }
        }.start()
    }

    companion object {
        const val MAX_TIME_MILLIS = 1L * 60 * 60 * 1000
        const val HIDE_CARDS_DELAY = 800L
    }
}