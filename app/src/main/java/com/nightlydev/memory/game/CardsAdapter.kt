package com.nightlydev.memory.game

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.nightlydev.memory.R
import com.nightlydev.memory.model.SelectableCard

/**
 * Created by edu
 */
class CardsAdapter : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {

    private var items : MutableList<SelectableCard> = mutableListOf()
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

    fun setItems(items: List<SelectableCard>?) {
        if (items == null) {
            this.items.clear()
        } else {
            this.items = items.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun setItem(item: SelectableCard, position: Int) {
        items[position] = item
        notifyItemChanged(position)
    }

    fun getItem(position: Int): SelectableCard? {
        if (position < 0 || position >= items.size) return null
        return items[position]
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val background: ImageView = itemView.findViewById(R.id.iv_background)

        fun bindCard(card: SelectableCard) {
            if (card.isSelected) {
                Glide.with(itemView).load(card.photoUrl).into(background)
            } else {
                background.setImageResource(R.drawable.ic_card_back)
            }

        }

        fun resetViews() {
            background.setImageResource(R.drawable.ic_card_back)
        }
    }
}