package com.edu.memory.ui.game

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.edu.memory.data.PhotosRepository
import com.edu.memory.data.ScoresRepository
import com.edu.memory.model.Difficulty
import javax.inject.Inject

/**
 * Created by edu
 */
class GameViewModelFactory
@Inject constructor(@GameActivity.DifficultyLevel val difficulty: Difficulty,
                    val photosRepository: PhotosRepository,
                    val scoresRepository: ScoresRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(difficulty, photosRepository, scoresRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.simpleName)
    }
}