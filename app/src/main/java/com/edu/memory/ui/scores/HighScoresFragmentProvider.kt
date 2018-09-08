package com.edu.memory.ui.scores

import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by edusevilla90
 */
@Module
abstract class HighScoresFragmentBuilder {

    @ContributesAndroidInjector
    abstract fun bindHighScoresFragmentFactory(): HighScoresFragment
}