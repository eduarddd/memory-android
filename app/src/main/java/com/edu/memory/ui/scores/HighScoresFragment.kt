package com.edu.memory.ui.scores

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edu.memory.R
import com.edu.memory.data.ScoresRepository
import com.edu.memory.model.Difficulty
import com.edu.memory.model.Score
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_high_scores.*
import javax.inject.Inject

/**
 * Created by edu
 */
class HighScoresFragment : DaggerFragment() {

    @Inject
    lateinit var scoresRepository: ScoresRepository
    private val scoresAdapter = ScoresAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_high_scores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        val difficulty = arguments?.getSerializable(ARG_DIFFICULTY) as Difficulty
        scoresRepository.getScores(difficulty).observe(this, Observer { displayScores(it) })
    }

    private fun displayScores(scores: List<Score>?) {
        scoresAdapter.scores = scores?.toMutableList() ?: mutableListOf()
    }

    private fun initRecyclerView() {
        rv_scores.apply {
            adapter = scoresAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    companion object {
        const val ARG_DIFFICULTY = "ARG_DIFFICULTY"

        fun newInstance(difficulty: Difficulty): HighScoresFragment {
            val fragment = HighScoresFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(ARG_DIFFICULTY, difficulty)
            }
            return fragment
        }
    }
}