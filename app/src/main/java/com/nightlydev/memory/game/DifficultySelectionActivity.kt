package com.nightlydev.memory.game

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nightlydev.memory.R
import com.nightlydev.memory.game.Difficulty.*
import kotlinx.android.synthetic.main.activity_difficulty_selection.*


class DifficultySelectionActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_difficulty_selection)

        bt_difficulty_easy.setOnClickListener { startGameActivity(Difficulty.EASY) }
        bt_difficulty_medium.setOnClickListener { startGameActivity(MEDIUM) }
        bt_difficulty_hard.setOnClickListener { startGameActivity(HARD) }
    }

    private fun startGameActivity(difficulty: Difficulty) {
        GameActivity.start(this, difficulty)
        finish()
    }
}