package com.edu.memory.data.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.edu.memory.model.Score

/**
 * Created by edu
 */
@Dao
interface ScoreDao {

    @Query("SELECT * FROM score WHERE difficulty = :difficulty ORDER BY timeInSeconds ASC")
    fun getScores(difficulty: String): LiveData<List<Score>>

    @Insert(onConflict = REPLACE)
    fun insert(score: Score)

    @Delete
    fun delete(score: Score)
}