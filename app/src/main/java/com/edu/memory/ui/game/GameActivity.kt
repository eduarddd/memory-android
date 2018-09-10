package com.edu.memory.ui.game

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import android.view.View
import com.edu.memory.R
import com.edu.memory.data.Status
import com.edu.memory.extensions.setVisible
import com.edu.memory.extensions.showToast
import com.edu.memory.extensions.toFormattedDuration
import com.edu.memory.model.Card
import com.edu.memory.model.Difficulty
import com.edu.memory.model.Score
import com.edu.memory.ui.CardItemDecoration
import com.edu.memory.ui.FlipAnimator
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_game.*
import javax.inject.Inject
import kotlin.math.sqrt

/**
 * Created by edu
 */
class GameActivity : DaggerAppCompatActivity() {

    private val cardsAdapter: CardsAdapter = CardsAdapter()
    @Inject
    lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        setSupportActionBar(toolbar_top)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = ""
        initViewModel()
        initRecyclerView()
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() {
        viewModel.cards.observe(this, Observer {
            when (it?.status) {
                Status.SUCCESS -> {
                    progress_bar.setVisible(false)
                    cardsAdapter.setItems(it.data)
                }
                Status.ERROR -> {
                    progress_bar.setVisible(false)
                    showToast("There was an error loading the game")
                    finish()
                }
                Status.LOADING -> progress_bar.setVisible(true)
            }
        })
        viewModel.timeInSeconds.observe(this, Observer { displayTime(it) })
        viewModel.pairFlipCount.observe(this, Observer { displayFlipCount(it) })
        viewModel.score.observe(this, Observer { if (it != null) onGameFinished(it) })
    }

    private fun initRecyclerView() {
        cardsAdapter.onItemClickListener = { position -> onCardSelected(position) }

        rv_cards.apply {
            setHasFixedSize(true)
            val columns = sqrt(viewModel.game.difficulty.pairCount * 2.0)
            layoutManager = GridLayoutManager(this@GameActivity, columns.toInt())
            adapter = cardsAdapter
            addItemDecoration(CardItemDecoration(this@GameActivity, R.dimen.card_grid_separation))
        }
    }

    private fun displayTime(timeInSeconds: Long?) {
        tv_time_count.text = timeInSeconds.toFormattedDuration()
    }

    private fun displayFlipCount(flipCount: Int?) {
        tv_flip_count.text = flipCount.toString()
    }

    private fun onCardSelected(position: Int) {
        val card = cardsAdapter.getItem(position) ?: return

        if (!viewModel.isCardFlipped(card)) {
            flipCard(card, true)
        }
        viewModel.onCardSelected(card).observe(this, Observer { pair ->
            if (pair == null) return@Observer
            flipCard(pair.first, false)
            flipCard(pair.second, false)
        })
    }

    private fun flipCard(card: Card, showFront: Boolean) {
        val position = cardsAdapter.getItemPosition(card)
        val cardView = rv_cards.layoutManager?.findViewByPosition(position)

        val front = cardView?.findViewById<View>(R.id.card_front)
        val back = cardView?.findViewById<View>(R.id.card_back)
        FlipAnimator.flipView(this, front, back, showFront)
    }

    private fun onGameFinished(score: Score) {
        val message = getString(R.string.dialog_message_game_finished, score.timeInSeconds.toFormattedDuration(), score.flipsCount)
        AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title_game_finished)
                .setMessage(message)
                .setPositiveButton(R.string.done) { _, _ -> finish() }
                .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            showStopGameConfirmation()
            false
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        showStopGameConfirmation()
    }

    private fun showStopGameConfirmation() {
        AlertDialog.Builder(this)
                .setMessage(R.string.alert_confirm_finish_game)
                .setPositiveButton(android.R.string.yes) { _, _ -> finish() }
                .setNegativeButton(android.R.string.no, null)
                .show()
    }

    companion object {
        const val EXTRA_DIFFICULTY = "EXTRA_DIFFICULTY"
        private const val STATE_DIFFICULTY = "STATE_DIFFICULTY"

        fun start(context: Context, difficulty: Difficulty) {
            val intent = Intent(context, GameActivity::class.java).apply {
                putExtra(EXTRA_DIFFICULTY, difficulty)
            }
            context.startActivity(intent)
        }
    }
}
