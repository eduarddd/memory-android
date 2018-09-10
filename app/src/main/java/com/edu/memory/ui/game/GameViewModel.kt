package com.edu.memory.ui.game

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.CountDownTimer
import com.edu.memory.data.PhotosRepository
import com.edu.memory.data.Resource
import com.edu.memory.data.ScoresRepository
import com.edu.memory.data.Status
import com.edu.memory.data.flickrapi.PhotoObject
import com.edu.memory.data.flickrapi.getDownloadUrl
import com.edu.memory.extensions.addItem
import com.edu.memory.model.*
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

    lateinit var game: Game
    val flips: MutableLiveData<List<Pair<Card, Card>>> = MutableLiveData()
    val cards: MediatorLiveData<Resource<List<Card>>> = MediatorLiveData()

    val score: MutableLiveData<Score> = MutableLiveData()

    val flippedCards: MutableLiveData<MutableList<Card>> = MutableLiveData()
    val gameAction: ActionLiveData<GameAction> = ActionLiveData()
    var selectedPair: Pair<Card, Card?>? = null

    val timeInSeconds: MutableLiveData<Long> = MutableLiveData()
    private lateinit var timer: CountDownTimer

    init {
        val searchPhotosSource = photosRepository.fetchPhotos("kittens", difficulty.pairCount)
        cards.addSource(searchPhotosSource) { photos ->
            when (photos?.status) {
                Status.ERROR -> cards.value = Resource.error("")
                Status.LOADING -> cards.value = Resource.loading()
                Status.SUCCESS -> {
                    cards.removeSource(searchPhotosSource)
                    onPhotosLoaded(photos.data)
                }
            }
        }
    }

    private fun onPhotosLoaded(photos: List<PhotoObject>?) {
        val pairs = initCards(photos)
        game = Game(startDate = Calendar.getInstance(), difficulty = difficulty, pairs = pairs)
        cards.value = Resource.success(game.shuffleCards())
        initTimer()
    }

    private fun initCards(photos: List<PhotoObject>?): List<Pair<Card, Card>> {
        val pairs = mutableListOf<Pair<Card, Card>>()
        photos?.let {
            for (i in 0 until difficulty.pairCount) {
                val photo = it[i]
                val cardA = Card(pairNumber = i, photoUrl = photo.getDownloadUrl())
                val cardB = Card(pairNumber = i, photoUrl = photo.getDownloadUrl())
                val pair = Pair(cardA, cardB)
                pairs.add(pair)
            }
        }
        return pairs
    }

    fun onCardSelected(card: Card) {
        if (flippedCards.value?.contains(card) == true) return
        gameAction.value = GameAction.select(card)
        if (selectedPair == null) {
            selectedPair = Pair(card, null)
            return
        } else {
            selectedPair = Pair(selectedPair!!.first, card)
            onPairSelected(Pair(selectedPair!!.first, card))
        }
    }

    private fun onPairSelected(pair: Pair<Card, Card>) {
        val flip = Flip(pair.first, pair.second)

        if (pair.first.pairNumber == pair.second.pairNumber) {
            flippedCards.addItem(pair.first)
            flippedCards.addItem(pair.second)
        } else {
            Timer().schedule(HIDE_CARDS_DELAY) {
                gameAction.postValue(GameAction.deselect(pair.first))
                gameAction.postValue(GameAction.deselect(pair.second))
            }
        }
        if (cards.value?.data?.size == flippedCards.value?.size) {
            onGameFinished()
        }

        flips.addItem(flip)
        selectedPair = null
    }

    private fun onGameFinished() {
        game.endDate = Calendar.getInstance()
        saveScore()
    }

    private fun saveScore() {
        val score = Score(game.difficulty, timeInSeconds.value, flips.value?.size)
        scoresRepository.saveScore(score)
        this.score.value = score
    }

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