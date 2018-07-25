package com.nightlydev.memory

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nightlydev.memory.model.Card

/**
 * Created by edu
 */
class GameViewModel : ViewModel() {

    val cards : MutableLiveData<List<Card>> = MutableLiveData()

    init {
        //Generate fake cards
        val fakeCards = mutableListOf<Card>()

        for (i in 0..7) {
            fakeCards.add(Card(i))
        }
        fakeCards.addAll(fakeCards)
        fakeCards.shuffle()

        this.cards.value = fakeCards
    }

    public fun checkPair(card1: Card, card2: Card) {

    }
}