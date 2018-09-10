package com.edu.memory.model

import java.io.Serializable
import java.util.*

/**
 * Created by edusevilla90
 */
data class Game

constructor(val id: Int = 0,
            val startDate: Calendar,
            var endDate: Calendar? = null,
            val difficulty: Difficulty,
            val pairs: List<Pair<Card, Card>>,
            var player: Player = Player(name = "Edu")) : Serializable


fun Game.getNumberOfCards(): Int {
    return difficulty.pairCount * 2
}

fun Game.getDurationInMillis(): Long {
    endDate?.let {
        return it.timeInMillis - startDate.timeInMillis
    }
    return -1
}

fun Game.shuffleCards(): List<Card> {
    val cards = mutableListOf<Card>()
    pairs.forEach {
        cards.add(it.first)
        cards.add(it.second)
    }
    return cards.shuffled()
}