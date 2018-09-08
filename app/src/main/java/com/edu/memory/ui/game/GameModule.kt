package com.edu.memory.ui.game

import android.arch.lifecycle.ViewModelProviders
import com.edu.memory.model.Difficulty
import com.edu.memory.ui.game.GameActivity.Companion.EXTRA_DIFFICULTY
import dagger.Module
import dagger.Provides

/**
 * Created by edusevilla90
 */
@Module
class GameModule {

    @Module
    companion object {
        @Provides
        fun provideGameViewModel(activity: GameActivity,
                                 factory: GameViewModelFactory): GameViewModel =
                ViewModelProviders.of(activity, factory).get(GameViewModel::class.java)
    }

    @Provides
    fun provideDifficulty(activity: GameActivity): Difficulty =
            activity.intent.getSerializableExtra(EXTRA_DIFFICULTY) as Difficulty
}


/*
@Module
public class RssHomeModule {

    @Provides
    static RssViewModel provideRssViewModel(RssHomeActivity rssHomeActivity, RssHomeViewModelFactory factory) {
        return ViewModelProviders.of(rssHomeActivity, factory).get(RssViewModel.class);
    }
}
 */