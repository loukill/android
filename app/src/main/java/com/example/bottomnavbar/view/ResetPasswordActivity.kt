package com.example.bottomnavbar.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.bottomnavbar.R
import com.example.bottomnavbar.api.Login
import com.example.bottomnavbar.api.RetrofitImp
import com.example.bottomnavbar.databinding.ActivityResetPasswordBinding
import com.example.bottomnavbar.model.User
import com.example.bottomnavbar.model.User1
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnResetPassword.setOnClickListener {

            // Update user information
            updateUser()




        }


    }


    private fun updateUser() {
        // Get user data from SharedPreferences
        // val sharedPreferences: SharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)

        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("token", "")
        val numTel = binding.editPassword.text.toString().trim()
        val password = binding.txtResetPasswordDesc.text.toString().trim()

        if (numTel.isEmpty() || password.isEmpty()) {
            // Show an error message or toast indicating that both fields are required
            showSnackbar("Both phone number and password are required.")
            return
        }
        Log.d("hhhhhhhhhhh",userId.toString())
        val user1 = User1(
            numTel = binding.editPassword.text.toString(), // Assurez-vous que userId n'est pas null
            password= binding.txtResetPasswordDesc.text.toString())

        val update = RetrofitImp.buildRetrofit().create(Login::class.java)
        update.updateUser2(user1).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    handleSuccessfulupdate(response.body())
                } else {
                    handleUpdateFailure(response.errorBody()?.string().toString())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Handle the failure here
                // You can log the error or show a message to the user
                handleUpdateFailure(t.message ?: "Unknown error")
            }
        })
    }


    private fun handleSuccessfulupdate(user: User?) {
        // Handle successful registration
        // Optionally, you can navigate to the login screen
        startActivity(Intent(this@ResetPasswordActivity, LoginActivity::class.java))
        finish()
    }

    private fun handleUpdateFailure(errorMessage: String) {
        // Handle registration failure
        showSnackbar(errorMessage)
    }
    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }



}