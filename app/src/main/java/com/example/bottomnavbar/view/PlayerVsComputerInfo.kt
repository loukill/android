package com.example.bottomnavbar.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.bottomnavbar.R
import com.example.bottomnavbar.api.ApiService
import com.example.bottomnavbar.model.ScoreTicTac
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlayerVsComputerInfo : AppCompatActivity() {
    companion object {
        private const val USER_NAME_KEY = "userName" // Assurez-vous que c'est la même clé que dans LoginActivity
    }
    lateinit var txtPlayerInfo: TextView
    lateinit var txtLevelInfo: TextView
    lateinit var txtScoreInfo: TextView
    lateinit var txtHighestScore: TextView
    lateinit var btnNextLevel: Button
    lateinit var btnHomePage: Button
    lateinit var sharedPreferences: SharedPreferences
    lateinit var sharedPreferences1: SharedPreferences

    var isScoreFinalSent = false
    var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_vs_computer_info)

        title = "Level Information"
        txtPlayerInfo = findViewById(R.id.txtPlayerInfo)
        txtLevelInfo = findViewById(R.id.txtLevelInfo)
        txtScoreInfo = findViewById(R.id.txtScoreInfo)
        btnNextLevel = findViewById(R.id.btnNextLevel)
        btnHomePage = findViewById(R.id.btnHomePage)
        txtHighestScore = findViewById(R.id.txtHighestScore);

        sharedPreferences = getSharedPreferences(getString(R.string.data_file), Context.MODE_PRIVATE)
        sharedPreferences1 = getSharedPreferences(getString(R.string.score_file), Context.MODE_PRIVATE)
        var isSameLevel = false

        sharedPreferences = getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        if(intent!=null){
            txtPlayerInfo.text = intent.getStringExtra("winnerInformation")
            score = intent.getStringExtra("score")?.toIntOrNull() ?: 0
            var level = intent.getStringExtra("level")
            var name = intent.getStringExtra("name")
            var highestScore = sharedPreferences1.getString("highestScore", score.toString())
            var isHigh = intent.getBooleanExtra("isHigh", false)

            if(name!=""){
                btnNextLevel.text = name
                isSameLevel = true
                btnNextLevel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_replay, 0,0,0);
            }
            else{
                btnNextLevel.text = "Next Level"
                btnNextLevel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_forward, 0,0,0);
                isSameLevel = false
            }
            txtLevelInfo.append(level)
            txtScoreInfo.append(score.toString())
            txtHighestScore.append(highestScore)
            txtHighestScore.append(")")

            if(isHigh){
//                AlertDialog.Builder(this).setCancelable(false).setTitle("Congratulations!").setMessage("Your new highscore: "+highestScore)
//                        .setIcon(R.drawable.ic_celebration).setPositiveButton("Ok",  DialogInterface.OnClickListener { dialog, which ->  dialog.dismiss()})
//                        .show()
                var sn = Snackbar.make(txtHighestScore, "Your New Highest Score: $highestScore", Snackbar.LENGTH_INDEFINITE)
                sn.setAction("Ok", View.OnClickListener {
                    sn.dismiss()
                }).show()
            }
        }
        btnNextLevel.setOnClickListener {
            var intent = Intent(this, PlayerVsComputerActivity::class.java)
            intent.putExtra("isSameLevel", isSameLevel)
            startActivity(intent)
            finish()
        }
        btnHomePage.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        AlertDialog.Builder(this).setMessage("Leave game?").setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
            sendScoreAndExit( score)
            sharedPreferences.edit().clear().apply()
            super.onBackPressed()
        }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }).setCancelable(false).show()
        return true
    }
    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).setMessage("Leave game?").setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->

            sendScoreAndExit(score)
            sharedPreferences.edit().clear().apply()
            super.onBackPressed()
        }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }).setCancelable(false).show()
    }

    fun sendScoreAndExit(score: Int?) {
        val userName = sharedPreferences.getString(USER_NAME_KEY, "DefaultUser")?: "DefaultUser"
        Log.d("ScoreSubmission", "Tentative d'envoi du score : $score pour l'utilisateur $userName")
        if (isScoreFinalSent) return

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)
        val scoreRequest = ScoreTicTac(userName, score ?: 0)

        service.addScore(scoreRequest).enqueue(object : Callback<ScoreTicTac> {
            override fun onResponse(call: Call<ScoreTicTac>, response: Response<ScoreTicTac>) {
                if (response.isSuccessful) {
                    isScoreFinalSent = true
                    Log.d("ScoreSubmission", "Score enregistré avec succès : ${response.body()}")
                    exitGame()
                } else {
                    Log.e("ScoreSubmission", "Échec de l'enregistrement du score : ${response.errorBody()}")
                    exitGame()
                }
            }

            override fun onFailure(call: Call<ScoreTicTac>, t: Throwable) {
                Log.e("ScoreSubmission", "Erreur lors de l'envoi du score : ${t.message}")
                exitGame()
            }
        })
    }

    private fun exitGame() {
        sharedPreferences.edit().clear().apply()
        super.onBackPressed()
    }
}