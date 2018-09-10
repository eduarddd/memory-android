package com.edu.memory.model

import java.io.Serializable

/**
 * Created by edu
 *
 * Data class representing a card.
 */
data class Card
constructor(val id: Int = 0, val pairNumber: Int, val photoUrl: String) : Serializable