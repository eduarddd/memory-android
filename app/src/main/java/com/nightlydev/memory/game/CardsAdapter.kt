package com.nightlydev.memory.game

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nightlydev.memory.GlideApp
import com.nightlydev.memory.R
import com.nightlydev.memory.extensions.determineCardItemWidth
import com.nightlydev.memory.extensions.determinegetCardItemHeight
import com.nightlydev.memory.extensions.inflateView
import com.nightlydev.memory.model.SelectableCard

/**
 * Created by edu
 */
class CardsAdapter : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {

    private var items : MutableList<SelectableCard> = mutableListOf()
    var onItemClickListener: ((View, Int) -> Unit)? = null

    override fun onCreateViewHolder(container: ViewGroup, p1: Int): CardViewHolder {
        val itemWidth = determineCardItemWidth(container, items.size)
        val itemHeight = determinegetCardItemHeight(container, items.size)

        return CardViewHolder(inflateView(container, R.layout.item_card)).apply {
            itemView.layoutParams.width = itemWidth
            itemView.layoutParams.height = itemHeight
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = items[position]

        holder.itemView.setOnClickListener { onItemClickListener?.invoke(it, position) }
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
            background.clipToOutline = true
            if (card.isSelected) {
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