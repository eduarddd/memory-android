package com.edu.memory.ui.game

import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.edu.memory.GlideApp
import com.edu.memory.R
import com.edu.memory.extensions.determineCardItemHeight
import com.edu.memory.extensions.determineCardItemWidth
import com.edu.memory.extensions.inflateView
import com.edu.memory.model.Card


/**
 * Created by edu
 */
class CardsAdapter : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {

    private var items: MutableList<Card> = mutableListOf()
    private var selectedItems: SparseBooleanArray = SparseBooleanArray()
    var onItemClickListener: ((Int, Card) -> Unit)? = null

    override fun onCreateViewHolder(container: ViewGroup, p1: Int): CardViewHolder {
        val itemWidth = determineCardItemWidth(container, items.size)
        val itemHeight = determineCardItemHeight(container, items.size)

        return CardViewHolder(inflateView(container, R.layout.item_card)).apply {
            itemView.layoutParams.width = itemWidth
            itemView.layoutParams.height = itemHeight
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = items[position]

        holder.itemView.setOnClickListener { onItemClickListener?.invoke(position, card) }
        holder.bindCard(card)
    }

    fun toggleSelection(card: Card, select: Boolean) {
        toggleSelection(getItemPosition(card), select)
    }

    fun toggleSelection(position: Int, select: Boolean) {
        if (select) {
            selectedItems.put(position, true)
        } else {
            selectedItems.delete(position)
        }
        notifyItemChanged(position)
    }

    fun isSelected(item: Card): Boolean {
        return selectedItems.indexOfKey(getItemPosition(item)) >= 0
    }

    fun getSelectedItems(): List<Card> {
        val result = mutableListOf<Card>()

        for (i in 0 until selectedItems.size()) {
            val index = selectedItems.keyAt(i)
            result.add(items[index])
        }
        return result
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

    fun getItemPosition(card: Card): Int {
        return items.indexOf(card)
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val background: ImageView = itemView.findViewById(R.id.iv_background)

        fun bindCard(card: Card) {
            background.clipToOutline = true

            if (isSelected(card)) {
                GlideApp.with(itemView.context)
                        .load(card.photoUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(background)
            } else {
                background.setImageResource(R.drawable.ic_card_back)
            }
        }
    }
}