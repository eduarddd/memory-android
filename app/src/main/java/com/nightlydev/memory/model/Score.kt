package com.nightlydev.memory.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import io.reactivex.annotations.NonNull

/**
 * @author edu (edusevilla90@gmail.com)
 * @since 1-8-18
 */
@Entity
data class Score(
        var difficulty: Difficulty,
        var timeInSeconds: Long,
        var flipsCount: Int) {

    @PrimaryKey
    @NonNull
    var id: Int = hashCode()
}