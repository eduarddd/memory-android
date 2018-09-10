package com.edu.memory.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import io.reactivex.annotations.NonNull

/**
 * Created by edu
 *
 * Data class holding the Scores of the game
 */
@Entity
data class Score(
        var difficulty: Difficulty,
        var timeInSeconds: Long?,
        var flipsCount: Int?) {

    @PrimaryKey
    @NonNull
    var id: Int = hashCode()
}