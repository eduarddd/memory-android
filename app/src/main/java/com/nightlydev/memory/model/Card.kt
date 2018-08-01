package com.nightlydev.memory.model

/**
 * Created by edu
 */
open class Card(val pairNumber: Int, val photoUrl: String = "")

class SelectableCard(pairNumber: Int,
                     photoUrl: String = "",
                     var isSelected: Boolean = false)
    : Card(pairNumber, photoUrl)