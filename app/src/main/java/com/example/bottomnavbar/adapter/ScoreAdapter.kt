package com.example.bottomnavbar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavbar.R

class ScoreAdapter(private val scores: List<Int>) : RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {
    class ScoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val scoreTextView: TextView = view.findViewById(R.id.scoreTextView)

        fun bind(score: Int) {
            scoreTextView.text = score.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_score, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        holder.bind(scores[position])
    }

    override fun getItemCount() = scores.size
}
