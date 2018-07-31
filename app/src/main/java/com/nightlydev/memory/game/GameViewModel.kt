package com.nightlydev.memory.game

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.CountDownTimer
import com.nightlydev.memory.data.PhotosRepository
import com.nightlydev.memory.data.Resource
import com.nightlydev.memory.data.Status
import com.nightlydev.memory.data.flickrapi.getDownloadUrl
import com.nightlydev.memory.model.SelectableCard

/**
 * Created by edu
 */
class GameViewModel(val difficulty: Difficulty) : ViewModel() {

    val cards: MediatorLiveData<Resource<List<SelectableCard>>> = MediatorLiveData()
    val pairs: MutableList<Pair<SelectableCard, SelectableCard>> = mutableListOf()
    lateinit var countDownTimer: CountDownTimer
    val timeInSeconds: MutableLiveData<Long> = MutableLiveData()
    var selectedPair: Pair<SelectableCard?, SelectableCard?> = Pair(null, null)
    private val photosRepository = PhotosRepository()

    init {
        initCards()
    }

    private fun initCards() {
        val searchSource = photosRepository.searchPhotos("kittens")
        cards.addSource(searchSource){ photos ->
            when(photos?.status) {
                Status.ERROR -> cards.value = Resource.error("")
                Status.LOADING -> cards.value = Resource.loading()
                Status.SUCCESS -> {
                    val newCards = mutableListOf<SelectableCard>()

                    for (i in 0 until difficulty.pairsCount) {
                        newCards.add(SelectableCard(id = i, photoUrl = photos.data?.get(i).getDownloadUrl()))
                    }
                    newCards.addAll(newCards)
                    newCards.shuffle()

                    onCardsCreated(newCards)
                    cards.removeSource(searchSource)
                }
            }
        }
    }

    private fun onCardsCreated(cards: List<SelectableCard>) {
        this.cards.value = Resource.success(cards)
        initCountDownTimer()
    }

    public fun onCardSelected(card: SelectableCard, position: Int) {

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
    }
}

fun Long?.toFormattedTime(): String {
    if (this == null) return "00:00"
    val minutes = (this / 60)
    val seconds = (this % 60)
    return "$minutes:$seconds"
}