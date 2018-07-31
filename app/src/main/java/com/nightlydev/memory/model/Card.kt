package com.nightlydev.memory.model

/**
 * Created by edu
 */
open class Card(val id: Int, val photoUrl: String = "") {

    override fun equals(other: Any?): Boolean = this.id == (other as? Card)?.id
    override fun hashCode(): Int = id.hashCode()
}

class SelectableCard(id: Int,
                     photoUrl: String = "",
                     var isSelected: Boolean = false)
    : Card(id, photoUrl)