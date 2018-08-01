package com.nightlydev.memory.game

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.nightlydev.memory.R
import com.nightlydev.memory.model.Difficulty
import com.nightlydev.memory.model.Difficulty.HARD
import com.nightlydev.memory.model.Difficulty.MEDIUM
import kotlinx.android.synthetic.main.activity_difficulty_selection.*


class DifficultySelectionActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_difficulty_selection)
        setSupportActionBar(toolbar_top)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.new_game)

        bt_difficulty_easy.setOnClickListener { startGameActivity(Difficulty.EASY) }
        bt_difficulty_medium.setOnClickListener { startGameActivity(MEDIUM) }
        bt_difficulty_hard.setOnClickListener { startGameActivity(HARD) }
    }

    private fun startGameActivity(difficulty: Difficulty) {
        GameActivity.start(this, difficulty)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}