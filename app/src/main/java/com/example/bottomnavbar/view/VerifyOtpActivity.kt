package com.example.bottomnavbar.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bottomnavbar.R
import com.example.bottomnavbar.databinding.ActivityVerifyOtpBinding

class VerifyOTP  : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnResetPassword.setOnClickListener {
            if (checkOTP()) {
                // Verification code is correct
                startActivity(Intent(this, ResetPasswordActivity::class.java))
            } else {
                // Verification code is incorrect
                Toast.makeText(this, "Incorrect verification code", Toast.LENGTH_SHORT).show()
            }
        }



    }



    private fun checkOTP(): Boolean {
        val enteredCode = buildVerificationCode()
        val storedCode = getStoredVerificationCode()

        return enteredCode == storedCode
    }

    private fun buildVerificationCode(): Int {
        val codeBuilder = StringBuilder()
        listOf(binding.Code)
            .forEach { codeBuilder.append(it.text.toString()) }

        return codeBuilder.toString().toIntOrNull() ?: 0
    }

    private fun getStoredVerificationCode(): Int {
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)

        return sharedPreferences.getString("resetCode", "0")?.toIntOrNull() ?: 0
    }



}