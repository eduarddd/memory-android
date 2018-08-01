package com.nightlydev.memory.game

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.CountDownTimer
import com.nightlydev.memory.data.PhotosRepository
import com.nightlydev.memory.data.Resource
import com.nightlydev.memory.data.ScoresRepository
import com.nightlydev.memory.data.Status
import com.nightlydev.memory.data.flickrapi.PhotoSearchResponse
import com.nightlydev.memory.data.flickrapi.getDownloadUrl
import com.nightlydev.memory.extensions.downloadPhotos
import com.nightlydev.memory.model.Difficulty
import com.nightlydev.memory.model.Score
import com.nightlydev.memory.model.SelectableCard
import java.util.*
import kotlin.concurrent.schedule

/**
 * Created by edu
 */
class GameViewModel(val difficulty: Difficulty) : ViewModel() {

    val cards: MediatorLiveData<Resource<List<SelectableCard>>> = MediatorLiveData()
    val timeInSeconds: MutableLiveData<Long> = MutableLiveData()
    val pairFlipCount: MutableLiveData<Int> = MutableLiveData()
    val score: MutableLiveData<Score> = MutableLiveData()
    private val flippedCards: MutableList<SelectableCard> = mutableListOf()
    private var firstSelectedCard: SelectableCard? = null

    private val photosRepository = PhotosRepository()
    private val scoreRepository = ScoresRepository()
    private lateinit var countDownTimer: CountDownTimer

    init {
        initGame()
        pairFlipCount.value = 0
    }

    fun onCardSelected(card: SelectableCard) {
        val cards = this.cards.value?.data!!
        cards[cards.indexOf(card)].isSelected = true
        this.cards.value = Resource.success(cards)

        firstSelectedCard = if (firstSelectedCard == null) {
            card
        } else {
            onPairSelected(Pair(firstSelectedCard!!, card))
            null
        }
    }

    private fun onPairSelected(pair: Pair<SelectableCard, SelectableCard>) {
        pairFlipCount.value = pairFlipCount.value?.plus(1)

        if (pair.first.pairNumber == pair.second.pairNumber) {
            flippedCards.add(pair.first)
            flippedCards.add(pair.second)
        } else {
            Timer().schedule(HIDE_CARDS_DELAY) { hideCards(pair) }
        }
        if (cards.value?.data?.size == flippedCards.size) {
            onGameFinished()
        }
    }

    private fun onGameFinished() {
        saveScore()
    }

    private fun saveScore() {
        val score = Score(difficulty, timeInSeconds.value!!, pairFlipCount.value!!)
        scoreRepository.saveScore(score)
        this.score.value = score
    }

    private fun hideCards(pair: Pair<SelectableCard, SelectableCard>) {
        val cards = this.cards.value?.data!!
        cards[cards.indexOf(pair.first)].isSelected = false
        cards[cards.indexOf(pair.second)].isSelected = false
        this.cards.postValue(Resource.success(cards))
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
        val cards = mutableListOf<SelectableCard>()

        for (i in 0 until difficulty.pairsCount) {
            val photo = photos?.get(i)
            val cardA = SelectableCard(i, photo.getDownloadUrl())
            val cardB = SelectableCard(i, photo.getDownloadUrl())
            cards.add(cardA)
            cards.add(cardB)
        }
        cards.shuffle()
        startGame(cards)
    }

    private fun startGame(cards: List<SelectableCard>) {
        this.cards.value = Resource.success(cards)
        initCountDownTimer()
    }

    private fun initCountDownTimer() {
        countDownTimer = object : CountDownTimer(MAX_TIME_MILLIS, 1000) {
            override fun onFinish() {
                //TODO: Game over?
            }
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (MAX_TIME_MILLIS - millisUntilFinished) / 1000
                timeInSeconds.value = seconds
            }
        }.start()
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer.cancel()
    }

    companion object {
        const val MAX_TIME_MILLIS = 1L * 60 * 60 * 1000
        const val HIDE_CARDS_DELAY = 800L
    }
}