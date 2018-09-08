package com.edu.memory

import android.arch.persistence.room.Room
import com.edu.memory.data.local.GameDatabase
import com.edu.memory.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication


/**
 * Created by edu
 */
class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
            DaggerAppComponent.builder().create(this)

    lateinit var gameDatabase: GameDatabase

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
    }
}