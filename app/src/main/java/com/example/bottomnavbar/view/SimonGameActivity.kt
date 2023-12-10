package com.example.bottomnavbar.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavbar.R
import com.example.bottomnavbar.api.RetrofitClient
import com.example.bottomnavbar.model.Score
import com.example.bottomnavbar.model.SimonGameModel
import com.example.bottomnavbar.viewModel.SimonGameViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SimonGameActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private val viewModel = SimonGameViewModel(SimonGameModel())
    private val userSequence = mutableListOf<String>()
    private val sequenceHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simon_game)

        supportActionBar?.hide()

        val categoryId = intent.extras?.getString("categoryId")

        val buttonStart = findViewById<Button>(R.id.buttonStart)
        buttonStart.setOnClickListener {
            startGame()
        }

        findViewById<Button>(R.id.buttonRed).setOnClickListener { onColorButtonClicked("rouge") }
        findViewById<Button>(R.id.buttonGreen).setOnClickListener { onColorButtonClicked("vert") }
        findViewById<Button>(R.id.buttonBlue).setOnClickListener { onColorButtonClicked("bleu") }
        findViewById<Button>(R.id.buttonYellow).setOnClickListener { onColorButtonClicked("jaune") }

    }

    private fun animateButton(color: String) {
        Log.d("SimonGameActivity", "animateButton: Animation du bouton $color.")
        val button = when(color) {
            "rouge" -> findViewById<Button>(R.id.buttonRed)
            "vert" -> findViewById<Button>(R.id.buttonGreen)
            "bleu" -> findViewById<Button>(R.id.buttonBlue)
            "jaune" -> findViewById<Button>(R.id.buttonYellow)
            else -> return
        }

        val animator = ObjectAnimator.ofFloat(button, "alpha", 0f, 1f).apply {
            duration = 1000
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = 0
        }

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                button.alpha = 1f
            }
        })

        animator.start()
    }

    private fun showSequence(sequence: List<String>) {
        Log.d("SimonGameActivity", "showSequence: Affichage de la séquence ${sequence}.")
        val delay = 500L
        var totalDelay = 0L

        sequence.forEach { color ->
            sequenceHandler.postDelayed({
                animateButton(color)
            }, totalDelay)
            totalDelay += delay
        }
    }

    private fun updateUI() {
        Log.d("SimonGameActivity", "updateUI: Mise à jour de l'interface utilisateur.")
        findViewById<TextView>(R.id.levelTextView).text = "Niveau: ${viewModel.currentLevel}"
        showSequence(viewModel.currentSequence)
    }


    private fun startGame() {
        Log.d("SimonGameActivity", "startGame: Le jeu commence.")

        mediaPlayer = MediaPlayer.create(this, R.raw.game_music).apply {
            setOnPreparedListener {
                Log.d("MediaPlayer", "Prêt à jouer")
                start()
            }
            setOnErrorListener { _, _, _ ->
                Log.e("MediaPlayer", "Erreur lors de la lecture")
                Toast.makeText(this@SimonGameActivity, "Erreur lors de la lecture de l'audio", Toast.LENGTH_SHORT).show()
                true
            }
        }

        findViewById<Button>(R.id.buttonStart).isEnabled = false
        userSequence.clear()
        viewModel.generateNewSequence() // Générer la première séquence ici
        updateUI() // Mettre à jour l'interface utilisateur ici

    }


    override fun onStop() {
        super.onStop()
        sequenceHandler.removeCallbacksAndMessages(null)
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }


    private fun onColorButtonClicked(color: String) {
        Log.d("SimonGameActivity", "onColorButtonClicked: Bouton $color appuyé.")
        userSequence.add(color)

        val isCorrectSoFar = userSequence.zip(viewModel.currentSequence)
            .all { (userColor, simonColor) -> userColor == simonColor }

        if (!isCorrectSoFar) {
            resetGame()
        } else if (userSequence.size == viewModel.currentSequence.size) {
            viewModel.incrementLevel()
            viewModel.generateNewSequence()
            userSequence.clear()
           updateUI()
        }

    }

    private fun resetGame() {
        val scoreValue = viewModel.currentLevel - 1
        saveScore(scoreValue)  // Sauvegarder le score dans SharedPreferences
        sendScoreToBackend(scoreValue)// Sauvegarder le score
        showGameOverDialog()  // Afficher la boîte de dialogue Game Over

        findViewById<Button>(R.id.buttonStart).isEnabled = true
        viewModel.currentLevel = 1
        viewModel.generateNewSequence()
        userSequence.clear()
        updateUI()
        mediaPlayer?.pause()
    }

    private fun sendScoreToBackend(scoreValue: Int) {
        val newScore = Score("username", scoreValue, System.currentTimeMillis()) // Remplacez "username" par l'identifiant de l'utilisateur réel si nécessaire
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.postScore(newScore)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Log.d("SimonGameActivity", "Score sent successfully: ${response.body()}")
                        Toast.makeText(this@SimonGameActivity, "Score enregistré avec succès", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorResponse = response.errorBody()?.string() ?: "Unknown error"
                        Log.e("SimonGameActivity", "Error sending score: $errorResponse")
                        Toast.makeText(this@SimonGameActivity, "Erreur lors de l'envoi du score", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("SimonGameActivity", "Exception lors de l'envoi du score", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SimonGameActivity, "Exception lors de l'envoi du score: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showScores() {
        val intent = Intent(this, ScoreListActivity::class.java)
        startActivity(intent)
    }

    private fun showGameOverDialog() {
        AlertDialog.Builder(this)
            .setTitle("Game Over")
            .setMessage("Votre score: ${viewModel.currentLevel - 1}")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton("Voir les scores") { _, _ ->
                showScores()
            }
            .show()
    }

    private fun saveScore(scoreValue: Int) {
        val sharedPreferences = getSharedPreferences("com.example.bottomnavbar", Context.MODE_PRIVATE)
        val gson = Gson()

        // Retrieve current scores and update them
        val scoresJson = sharedPreferences.getString("scores_list", "[]")
        Log.d("SimonGameActivity", "Retrieved scores JSON: $scoresJson")
        val type = object : TypeToken<List<Score>>() {}.type
        val currentScores: MutableList<Score> = gson.fromJson(scoresJson, type) ?: mutableListOf()

        // Create a new Score object (add appropriate username and date)
        val newScore = Score("username", scoreValue, System.currentTimeMillis())
        currentScores.add(newScore)

        // Truncate the list if it exceeds 10 items
        if (currentScores.size > 10) {
            currentScores.removeAt(0)
        }

        // Save the updated list of scores
        sharedPreferences.edit().putString("scores_list", gson.toJson(currentScores)).apply()

        // Update best score if the new score is higher
        val bestScore = sharedPreferences.getInt("best_score", 0)
        if (scoreValue > bestScore) {
            sharedPreferences.edit().putInt("best_score", scoreValue).apply()
        }
    }


}
