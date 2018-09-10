package com.edu.memory.model

/**
 * Created by edu
 *
 * Represents the differents levels of difficulty of the game
 */
enum class Difficulty(val pairCount: Int) {
    EASY(pairCount = 8),
    MEDIUM(pairCount = 18),
    HARD(pairCount = 32)
}