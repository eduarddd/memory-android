package com.edu.memory

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.edu.memory.extensions.showToast
import com.edu.memory.game.DifficultySelectionActivity
import com.edu.memory.scores.HighScoresActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by edu
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initButtons()
    }

    private fun initButtons() {
        bt_play.setOnClickListener { startDifficultySelectionActivity() }
        bt_scores.setOnClickListener { startHighScoresActivity() }
        bt_options.setOnClickListener { showToast("Options") }
    }

    private fun startDifficultySelectionActivity() {
        startActivity(Intent(this, DifficultySelectionActivity::class.java))
    }

    private fun startHighScoresActivity() {
        startActivity(Intent(this, HighScoresActivity::class.java))
    }
}