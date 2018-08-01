package com.nightlydev.memory.model

/**
 * Created by edu
 */
enum class Difficulty(val pairsCount: Int) {
    EASY(pairsCount = 8),
    MEDIUM(pairsCount = 18),
    HARD(pairsCount = 32)
}