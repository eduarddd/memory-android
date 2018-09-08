package com.edu.memory.data.local

import com.edu.memory.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by edusevilla90
 */
@Module
class DatabaseModule {

    @Module
    companion object {
        @JvmStatic
        @Provides
        @Singleton
        fun provideScoreDao(database: GameDatabase): ScoreDao = database.scoreDao()

        @JvmStatic
        @Provides
        @Singleton
        fun provideDatabase(app: App): GameDatabase = app.gameDatabase
    }
}
