package com.edu.memory.game

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
import com.edu.memory.model.Difficulty
import com.edu.memory.model.Score
import com.edu.memory.model.Card
import java.util.*
import kotlin.concurrent.schedule

/**
 * Created by edu
 */
class GameViewModel(val difficulty: Difficulty) : ViewModel() {

    val cards: MediatorLiveData<Resource<List<Card>>> = MediatorLiveData()
    val timeInSeconds: MutableLiveData<Long> = MutableLiveData()
    val pairFlipCount: MutableLiveData<Int> = MutableLiveData()
    val score: MutableLiveData<Score> = MutableLiveData()


    private val flippedCards: MutableList<Card> = mutableListOf()
    private var tmpSelectedCard: Card? = null

    private val photosRepository = PhotosRepository()
    private val scoreRepository = ScoresRepository()
    private lateinit var timer: CountDownTimer

    init {
        initGame()
        pairFlipCount.value = 0
    }

    fun onCardSelected(card: Card): LiveData<Pair<Card, Card>> {
        val result = MutableLiveData<Pair<Card, Card>>()

        if (tmpSelectedCard == null) {
            tmpSelectedCard = card
        } else {
            if (card != tmpSelectedCard) {
                return onPairSelected(Pair(tmpSelectedCard!!, card))
            }
        }
        return result
    }

    fun isCardSelected(card: Card): Boolean {
        return card == tmpSelectedCard || flippedCards.contains(card)
    }

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
        tmpSelectedCard = null
        return result
    }

    private fun onGameFinished() {
        saveScore()
    }

    private fun saveScore() {
        val score = Score(difficulty, timeInSeconds.value!!, pairFlipCount.value!!)
        scoreRepository.saveScore(score)
        this.score.value = score
    }

    private fun initGame() {
        val searchPhotosSource = photosRepository.searchPhotos("kittens")
        cards.addSource(searchPhotosSource){ photos ->
            when(photos?.status) {
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
            val cardA = Card(i, photo.getDownloadUrl())
            val cardB = Card(i, photo.getDownloadUrl())
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