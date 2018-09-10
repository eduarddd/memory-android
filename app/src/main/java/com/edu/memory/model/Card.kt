package com.edu.memory.model

import java.io.Serializable
import java.util.*

/**
 * Created by edu
 *
 * Data class representing a card.
 */
data class Card
constructor(val id: Int = Random().nextInt(), val pairNumber: Int, val photoUrl: String) : Serializable