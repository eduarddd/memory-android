package com.nightlydev.memory.scores

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.nightlydev.memory.R
import com.nightlydev.memory.model.Difficulty
import kotlinx.android.synthetic.main.activity_high_scores.*

/**
 * @author edu (edusevilla90@gmail.com)
 * @since 1-8-18
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

    inner class DifficultyPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
        override fun getCount(): Int = Difficulty.values().size

        override fun getItem(i: Int): Fragment {
            return HighScoresFragment.newInstance(Difficulty.values()[i])
        }
    }
}