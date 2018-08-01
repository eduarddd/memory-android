package com.edu.memory.scores

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.edu.memory.R
import com.edu.memory.model.Difficulty
import kotlinx.android.synthetic.main.activity_high_scores.*

/**
* Created by edu
*/
class HighScoresActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_scores)
        setSupportActionBar(toolbar_top)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViewPager()
    }

    private fun initViewPager() {
        val adapter = DifficultyPagerAdapter(supportFragmentManager)
        view_pager.adapter = adapter
        tab_layout.setupWithViewPager(view_pager)
        for(i in 0 until adapter.count) {
            tab_layout.getTabAt(i)?.text = Difficulty.values()[i].name
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            false
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    inner class DifficultyPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
        override fun getCount(): Int = Difficulty.values().size

        override fun getItem(i: Int): Fragment {
            return HighScoresFragment.newInstance(Difficulty.values()[i])
        }
    }
}