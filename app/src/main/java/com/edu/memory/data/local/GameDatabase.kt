package com.edu.memory.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import com.edu.memory.model.Difficulty
import com.edu.memory.model.Score

/**
 * Created by edu
 */
@Database(entities = [Score::class], version = 1)
@TypeConverters(Converters::class)
abstract class GameDatabase : RoomDatabase() {

    abstract fun scoreDao(): ScoreDao
}

class Converters {
    @TypeConverter
    fun fromDifficultyName(name: String): Difficulty {
        return Difficulty.valueOf(name)
    }

    @TypeConverter
    fun fromDifficulty(difficulty: Difficulty): String {
        return difficulty.name
    }
}
