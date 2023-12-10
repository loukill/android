package com.example.bottomnavbar.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavbar.R
import com.example.bottomnavbar.adapter.ScoreAdapter
import com.example.bottomnavbar.model.Score
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ScoreListActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var scores: List<Score>
    private var bestScore: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_list)

        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("com.example.bottomnavbar", Context.MODE_PRIVATE)

        try {
            scores = retrieveScores()
            bestScore = getBestScore()
        } catch (e: Exception) {
            Log.e("ScoreListActivity", "Error in retrieving scores", e)
            scores = emptyList()
        }

        val adapter = ScoreAdapter(scores)
        findViewById<RecyclerView>(R.id.scoresRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@ScoreListActivity)
            this.adapter = adapter
        }

        findViewById<TextView>(R.id.bestScoreTextView).text = "Meilleur score : $bestScore"
    }


    private fun retrieveScores(): List<Score> {
        val scoresJson = sharedPreferences.getString("scores_list", "[]")
        Log.d("ScoreListActivity", "Retrieved scores JSON: $scoresJson")
        val gson = Gson()
        val type = object : TypeToken<List<Score>>() {}.type
        return gson.fromJson(scoresJson, type) ?: emptyList()
    }

    private fun getBestScore(): Int {
        return sharedPreferences.getInt("best_score", 0)
    }

}
