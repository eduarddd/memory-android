package com.nightlydev.memory.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import com.nightlydev.memory.model.Difficulty
import com.nightlydev.memory.model.Score

/**
 * @author edu (edusevilla90@gmail.com)
 * @since 1-8-18
 *
 * @Database(entities = [(CryptoCoin::class), FavoriteCryptoCoin::class], version = 1)
abstract class CryptoCoinDatabase : RoomDatabase() {
abstract fun cryptoCoinDao(): CryptoCoinDao
abstract fun favoriteCryptoCoinDao(): FavoriteCryptoCoinDao
}
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

    @TypeConverter fun fromDifficulty(difficulty: Difficulty): String {
        return difficulty.name
    }
}
