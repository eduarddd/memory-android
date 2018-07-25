package com.nightlydev.memory

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nightlydev.memory.extensions.showToast
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
        bt_play.setOnClickListener { startGameActivity() }
        bt_scores.setOnClickListener { showToast("High scores!") }
        bt_options.setOnClickListener { showToast("Options") }
    }

    private fun startGameActivity() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }
}