package com.example.bottomnavbar.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.bottomnavbar.R
import com.example.bottomnavbar.api.Login
import com.example.bottomnavbar.api.RetrofitImp
import com.example.bottomnavbar.databinding.ActivityForgetpasswordBinding
import com.example.bottomnavbar.model.User
import com.example.bottomnavbar.model.User3
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetpasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetpasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgetpasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnReturn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnResetPassword.setOnClickListener {


            RetrofitImp.buildRetrofit().create(Login::class.java).sendResetCode(
                User3(numTel = binding.tiEmail.text.toString())
            ).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {

                        val user = response.body()
                        Log.d("ssssssssssssssssss", user.toString())
                        user?.let {
                            // Save values to shared preferences
                            val sharedPreferences =
                                getSharedPreferences("user_data", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("userId", it._id)
                            editor.putString("resetCode", it.resetCode)


                            editor.apply()

                            // Start the User_Validation activity
                            startActivity(
                                Intent(
                                    this@ForgetpasswordActivity,
                                    VerifyOTP::class.java
                                )
                            )
                            finish()
                        } ?: run {
                            Toast.makeText(
                                this@ForgetpasswordActivity,
                                "User Not Found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Handle the response in case of an error
                        Toast.makeText(
                            this@ForgetpasswordActivity,
                            "Error: ${response.code()} - ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    // Handle the failure case
                    Toast.makeText(
                        this@ForgetpasswordActivity,
                        "Network Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}