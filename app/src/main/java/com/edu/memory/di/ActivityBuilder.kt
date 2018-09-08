package com.edu.memory.di

import com.edu.memory.ui.game.GameActivity
import com.edu.memory.ui.game.GameModule
import com.edu.memory.ui.scores.HighScoresActivity
import com.edu.memory.ui.scores.HighScoresFragmentBuilder
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by edusevilla90
 */
@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [GameModule::class])
    abstract fun bindGameActivity(): GameActivity

    @ContributesAndroidInjector(modules = [HighScoresFragmentBuilder::class])
    abstract fun bindHighScoresActivty(): HighScoresActivity
}