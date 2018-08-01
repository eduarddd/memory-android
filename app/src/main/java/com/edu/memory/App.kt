package com.edu.memory

import android.app.Application
import android.arch.persistence.room.Room
import com.edu.memory.data.db.GameDatabase

/**
 * @author edu (edusevilla90@gmail.com)
 * @since 31-7-18
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        initDatabase()
    }

    private fun initDatabase() {
        gameDatabase = Room.databaseBuilder(this, GameDatabase::class.java, "game-db")
                .fallbackToDestructiveMigration()
                .build()
    }

    companion object {
        lateinit var instance: App
            private set

        lateinit var gameDatabase: GameDatabase
    }
}