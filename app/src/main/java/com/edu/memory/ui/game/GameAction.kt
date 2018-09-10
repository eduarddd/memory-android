package com.edu.memory.ui.game

import com.edu.memory.model.Card

/**
 * Created by edusevilla90 on 9/10/18.
 */
enum class Action {
    SELECT_CARD,
    DESELECT_CARD
}

data class GameAction(val action: Action,
                      val card: Card) {

    companion object {
        fun select(card: Card) = GameAction(Action.SELECT_CARD, card)

        fun deselect(card: Card) = GameAction(Action.DESELECT_CARD, card)
    }
}
