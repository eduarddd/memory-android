package com.nightlydev.memory

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import com.nightlydev.memory.model.Card
import com.nightlydev.memory.ui.CardItemDecoration
import kotlinx.android.synthetic.main.activity_game.*

/**
 * Created by edu
 *
 *
 * todo: add "flip" animation when clicking on a card
 */
class GameActivity : AppCompatActivity() {

    lateinit var cardsAdapter: CardsAdapter
    lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        setSupportActionBar(toolbar_top)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = ""
        initRecyclerView()
        initViewModel(savedInstanceState)
    }

    private fun initRecyclerView() {
        cardsAdapter = CardsAdapter()
        cardsAdapter.onItemClickListener = { _, pos ->
            onCardSelected(cardsAdapter.getItem(pos), pos)
        }

        rv_cards.apply {
            //todo: calculate columns(spanCount) depending on level etc...
            layoutManager = GridLayoutManager(this@GameActivity, 4)
            adapter = cardsAdapter
            addItemDecoration(CardItemDecoration(this@GameActivity, R.dimen.card_grid_separation))
        }
    }

    private fun initViewModel(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
        viewModel.cards.observe(this, Observer { cards -> cardsAdapter.setItems(cards) })
    }

    private fun onCardSelected(card: Card?, position: Int) {
        if (card == null) return
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return false
        }
        return super.onOptionsItemSelected(item)
    }
}