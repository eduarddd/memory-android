package com.edu.memory.game

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.edu.memory.GlideApp
import com.edu.memory.R
import com.edu.memory.extensions.determineCardItemWidth
import com.edu.memory.extensions.determinegetCardItemHeight
import com.edu.memory.extensions.inflateView
import com.edu.memory.model.Card

/**
 * Created by edu
 */
class CardsAdapter : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {

    private var items : MutableList<Card> = mutableListOf()
    var onItemClickListener: ((View, Int) -> Unit)? = null

    override fun onCreateViewHolder(container: ViewGroup, p1: Int): CardViewHolder {
        val itemWidth = determineCardItemWidth(container, items.size)
        val itemHeight = determinegetCardItemHeight(container, items.size)

        return CardViewHolder(inflateView(container, R.layout.item)).apply {
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

    fun setItems(items: List<Card>?) {
        if (items == null) {
            this.items.clear()
        } else {
            this.items = items.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun setItem(item: Card, position: Int) {
        items[position] = item
        notifyItemChanged(position)
    }

    fun getItem(position: Int): Card? {
        if (position < 0 || position >= items.size) return null

        return items[position]
    }

    fun getItemPosition(card: Card): Int {
        return items.indexOf(card)
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val frontImageView: ImageView = itemView.findViewById(R.id.iv_background)
        private val backImageView: ImageView = itemView.findViewById(R.id.iv_background_back)

        fun bindCard(card: Card) {
            frontImageView.clipToOutline = true
            GlideApp.with(itemView.context)
                    .load(card.photoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(frontImageView)
            backImageView.setImageResource(R.drawable.ic_card_back)
        }
    }
}