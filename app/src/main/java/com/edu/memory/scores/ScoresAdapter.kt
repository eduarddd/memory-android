package com.edu.memory.scores

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.edu.memory.R
import com.edu.memory.extensions.inflateView
import com.edu.memory.extensions.toFormattedTime
import com.edu.memory.model.Score

/**
 * Created by edu
 */
class ScoresAdapter : RecyclerView.Adapter<ScoresAdapter.ScoreViewHolder>() {
    var scores: MutableList<Score> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(container: ViewGroup, p1: Int): ScoreViewHolder {
        return ScoreViewHolder(inflateView(container, R.layout.item_score))
    }

    override fun getItemCount(): Int = scores.size

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = scores[position]
        holder.resetViews()
        holder.bindScore(score)
    }

    inner class ScoreViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val ranking: TextView = itemView.findViewById(R.id.tv_ranking)
        private val time: TextView = itemView.findViewById(R.id.tv_time_count)
        private val flips: TextView = itemView.findViewById(R.id.tv_flip_count)

        fun bindScore(score: Score) {
            ranking.text = (adapterPosition + 1).toString()
            time.text = score.timeInSeconds.toFormattedTime()
            flips.text = score.flipsCount.toString()
        }

        fun resetViews() {
            ranking.text = null
            time.text = null
            flips.text = null
        }
    }
}