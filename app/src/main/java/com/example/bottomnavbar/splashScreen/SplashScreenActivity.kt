package com.example.bottomnavbar.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.bottomnavbar.R
import com.example.bottomnavbar.view.LoginActivity

class splashscreenActivity : AppCompatActivity() {

    private val SPLASH_SCREEN_DELAY: Long = 2000 // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            // Start the main activity after the splash screen duration
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN_DELAY)
    }
}