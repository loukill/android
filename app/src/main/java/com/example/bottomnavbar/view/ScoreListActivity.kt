package com.example.bottomnavbar.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavbar.R
import com.example.bottomnavbar.adapter.ScoreAdapter
import com.google.gson.Gson

class ScoreListActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_list)

        sharedPreferences = getSharedPreferences("com.example.bottomnavbar", Context.MODE_PRIVATE)

        val scores = retrieveScores()
        val bestScore = getBestScore()

        val adapter = ScoreAdapter(scores)
        findViewById<RecyclerView>(R.id.scoresRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@ScoreListActivity)
            this.adapter = adapter // Utilisation de 'this' pour clarifier la référence
        }

        findViewById<TextView>(R.id.bestScoreTextView).text = "Meilleur score : $bestScore"
    }

    private fun retrieveScores(): List<Int> {
        val scoresJson = sharedPreferences.getString("scores_list", "[]")
        val gson = Gson()
        return gson.fromJson(scoresJson, Array<Int>::class.java).toList()
    }

    private fun getBestScore(): Int {
        return sharedPreferences.getInt("best_score", 0)
    }
}

