package com.edu.memory.model

/**
 * Created by edu
 *
 * Represents the differents levels of difficulty of the game
 */
enum class Difficulty(val pairsCount: Int) {
    EASY(pairsCount = 8),
    MEDIUM(pairsCount = 18),
    HARD(pairsCount = 32)
}