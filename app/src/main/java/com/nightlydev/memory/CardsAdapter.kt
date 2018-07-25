package com.nightlydev.memory

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nightlydev.memory.model.Card

/**
 * Created by edu
 */
class CardsAdapter : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {

    private var items : MutableList<Card> = mutableListOf()
    var onItemClickListener: ((View, Int) -> Unit)? = null

    override fun onCreateViewHolder(container: ViewGroup, p1: Int): CardViewHolder {
        val itemView = LayoutInflater.from(container.context).inflate(R.layout.item_card, container, false)
        return CardViewHolder(itemView)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = items[position]
        holder.itemView.setOnClickListener { onItemClickListener?.invoke(it, position) }
        holder.resetViews()
        holder.bindCard(card)
    }

    fun setItems(items: List<Card>?) {
        if (items == null) {
            this.items.clear()
        } else {
            this.items = items.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Card? {
        if (position < 0 || position >= items.size) return null
        return items[position]
    }

    //todo: bind cards properly depending if it is selected or not(front or back part of the card)
    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardNumberText: TextView = itemView.findViewById(R.id.tv_card_number)

        fun bindCard(card: Card) {
            cardNumberText.text = card.id.toString()
        }

        fun resetViews() {
            cardNumberText.text = null
        }
    }
}