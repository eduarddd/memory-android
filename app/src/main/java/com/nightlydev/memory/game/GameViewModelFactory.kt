package com.nightlydev.memory.game

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

/**
 * Created by edu
 */
class GameViewModelFactory(val difficulty: Difficulty) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(difficulty) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.simpleName)
    }
}