package com.nightlydev.memory.game

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import com.nightlydev.memory.R
import com.nightlydev.memory.data.Status
import com.nightlydev.memory.extensions.setVisible
import com.nightlydev.memory.extensions.showToast
import com.nightlydev.memory.extensions.toFormattedTime
import com.nightlydev.memory.model.Difficulty
import com.nightlydev.memory.model.SelectableCard
import com.nightlydev.memory.ui.CardItemDecoration
import kotlinx.android.synthetic.main.activity_game.*
import kotlin.math.sqrt

/**
 * Created by edu
 *
 * todo: add "flip" animation when clicking on a card
 * todo: save state of game
 */
class GameActivity : AppCompatActivity() {

    private val cardsAdapter: CardsAdapter = CardsAdapter()
    private lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        setSupportActionBar(toolbar_top)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = ""
        initViewModel(savedInstanceState)
        initRecyclerView()
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel(savedInstanceState: Bundle?) {
        val difficulty = savedInstanceState?.getSerializable(STATE_DIFFICULTY) as? Difficulty
                ?: intent.getSerializableExtra(EXTRA_DIFFICULTY) as Difficulty
        val factory = GameViewModelFactory(difficulty)
        viewModel = ViewModelProviders.of(this, factory).get(GameViewModel::class.java)

        viewModel.cards.observe(this, Observer {
            when(it?.status) {
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
    }

    private fun initRecyclerView() {
        cardsAdapter.onItemClickListener = { _, pos -> onCardSelected(cardsAdapter.getItem(pos)) }

        rv_cards.apply {
            setHasFixedSize(true)
            val columns = sqrt(viewModel.difficulty.pairsCount * 2.0)
            layoutManager = GridLayoutManager(this@GameActivity, columns.toInt())
            adapter = cardsAdapter
            addItemDecoration(CardItemDecoration(this@GameActivity, R.dimen.card_grid_separation))
        }
    }

    private fun displayTime(timeInSeconds: Long?) {
        tv_time_count.text = timeInSeconds.toFormattedTime()
    }

    private fun displayFlipCount(flipCount: Int?) {
        tv_flip_count.text = flipCount.toString()
    }

    private fun onCardSelected(card: SelectableCard?) {
        if (card == null) return
        //cardsAdapter.setItem(card, position)
        viewModel.onCardSelected(card)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            showStopGameConfirmation()
            false
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(STATE_DIFFICULTY, viewModel.difficulty)
    }

    private fun showStopGameConfirmation() {
        AlertDialog.Builder(this)
                .setMessage(R.string.alert_confirm_finish_game)
                .setPositiveButton(android.R.string.yes) { _,_ -> finish() }
                .setNegativeButton(android.R.string.no, null)
                .show()
    }

    companion object {
        private const val EXTRA_DIFFICULTY = "EXTRA_DIFFICULTY"
        private const val STATE_DIFFICULTY = "STATE_DIFFICULTY"

        fun start(context: Context, difficulty: Difficulty) {
            val intent = Intent(context, GameActivity::class.java).apply {
                putExtra(EXTRA_DIFFICULTY, difficulty)
            }
            context.startActivity(intent)
        }
    }
}
