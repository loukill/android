package com.example.bottomnavbar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavbar.R
import com.example.bottomnavbar.model.Score
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale




class ScoreAdapter(private val scores: List<Score>) : RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {
    class ScoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val scoreTextView: TextView = view.findViewById(R.id.itemScoreTextView)
        val dateTextView: TextView = view.findViewById(R.id.itemDateTextView)

        fun bind(score: Score) {
            scoreTextView.text = score.score.toString()
            dateTextView.text = formatDate(score.date)
        }

        private fun formatDate(timestamp: Long): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_score, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = scores[position]
        holder.bind(score)
    }

    override fun getItemCount() = scores.size
}
