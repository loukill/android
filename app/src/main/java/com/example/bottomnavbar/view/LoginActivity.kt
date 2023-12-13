package com.example.bottomnavbar.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.bottomnavbar.MainActivity
import com.example.bottomnavbar.R
import com.example.bottomnavbar.api.Login
import com.example.bottomnavbar.api.RetrofitImp
import com.example.bottomnavbar.databinding.ActivityLoginBinding
import com.example.bottomnavbar.model.User
import com.example.bottomnavbar.model.User1
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    // Define a shared preference name
    private val PREF_NAME = "user_pref"
    private val USER_ID_KEY = "userId"
    private val USER_NAME_KEY = "userName"
    private val USER_EMAIL_KEY = "userEmail"
    private val USER_IMAGE_KEY = "userImageRes"
    private val USER_TOKEN_KEY = "tokenLogin"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val rootView = window.decorView.rootView


        binding.btnLogin.setOnClickListener {

            RetrofitImp.buildRetrofit().create(Login::class.java).login(
                User1(
                    numTel = binding.edtEmail.text.toString(),
                    password = binding.tiPassword.text.toString()


                )
            ).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        if (user != null) {
                            Log.d("eeeeeeeeeee",response.body().toString())
                            // Access the "user" property directly
                            val userObject = user
                            if (user != null) {

                                // Pass the user data to UserProfil activity
                                saveUserToPreferences(userObject)
                                Log.d("LoginActivity", "Login successful. User data: $userObject")
                                Log.d("bbbbbbbbb", userObject.toString())

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.putExtra("userId", userObject._id)
                                intent.putExtra("userName", userObject.name)
                                intent.putExtra("userEmail", userObject.email)
                                //intent.putExtra("userImageRes", userObject.imageRes)
                                startActivity(intent)
                                finish()
                            } else {
                                // Handle the case where the "user" property is null
                                Log.e("LoginActivity", "User object is null")

                                Snackbar.make(
                                    binding.contextView,
                                    "User object is null",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            // Handle the case where the entire response body is null
                            Log.e("LoginActivity", "Response body is null")

                            Snackbar.make(
                                binding.contextView,
                                "Response body is null",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        // Handle the case where the response is not successful
                        val errorBody = response.errorBody()?.string().toString()
                        Log.d("LoginActivity", "Login failed. Error: $errorBody")

                        Snackbar.make(
                            binding.contextView,
                            errorBody,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }


                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("LoginActivity", "Login request failed", t)

                    Snackbar.make(
                        binding.contextView,
                        t.message.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })


        }

        binding.btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
        binding.btnForgotPassword.setOnClickListener {

            startActivity(Intent(this, ForgetpasswordActivity::class.java))

        }

    }



    private fun validateEmail(): Boolean {
        val email = binding.edtEmail.text.toString().trim()

        if (email.isEmpty()) {
            binding.tiEmailLayout.error = getString(R.string.msg_must_not_be_empty)
            binding.edtEmail.requestFocus()
            return false
        } else {
            binding.tiEmailLayout.isErrorEnabled = false
        }

        // Improved email validation using Regex
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        if (!email.matches(emailRegex.toRegex())) {
            binding.tiEmailLayout.error = getString(R.string.msg_invalid_email)
            binding.edtEmail.requestFocus()
            return false
        } else {
            binding.tiEmailLayout.isErrorEnabled = false
        }

        return true
    }


    private fun validatePassword(): Boolean {
        val password = binding.tiPassword.text.toString().trim()

        if (password.isEmpty()) {
            binding.tiPasswordLayout.error = getString(R.string.msg_must_not_be_empty)
            binding.tiPassword.requestFocus()
            return false
        } else {
            binding.tiPasswordLayout.isErrorEnabled = false
        }

        if (password.length < 6) {
            binding.tiPasswordLayout.error = getString(R.string.msg_password_length)
            binding.tiPassword.requestFocus()
            return false
        } else {
            binding.tiPasswordLayout.isErrorEnabled = false
        }

        // Ajouter des critères de complexité, par exemple :
        // Exiger au moins une lettre majuscule
        if (!password.any { it.isUpperCase() }) {
            binding.tiPasswordLayout.error = getString(R.string.msg_password_uppercase)
            binding.tiPassword.requestFocus()
            return false
        } else {
            binding.tiPasswordLayout.isErrorEnabled = false
        }

        // Exiger au moins un chiffre
        if (!password.any { it.isDigit() }) {
            binding.tiPasswordLayout.error = getString(R.string.msg_password_digit)
            binding.tiPassword.requestFocus()
            return false
        } else {
            binding.tiPasswordLayout.isErrorEnabled = false
        }

        // Exiger au moins un caractère spécial
        val specialCharacters = "!@#$%^&*()-_+=<>?/{}|"
        if (!password.any { specialCharacters.contains(it) }) {
            binding.tiPasswordLayout.error = getString(R.string.msg_password_special_char)
            binding.tiPassword.requestFocus()
            return false
        } else {
            binding.tiPasswordLayout.isErrorEnabled = false
        }

        return true
    }
    private fun saveUserToPreferences(user: User) {
        val sharedPreferences: SharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(USER_ID_KEY, user._id)
        editor.putString(USER_NAME_KEY, user.name)
        editor.putString(USER_EMAIL_KEY, user.email)
        //editor.putString(USER_IMAGE_KEY, user.imageRes)
        editor.putString(USER_TOKEN_KEY, user.token)
        editor.apply()

        // Add logs to check values after saving
        Log.d("UserProfileFragment", "Saved UserID: ${user._id}, UserName: ${user.name}, UserEmail: ${user.email}, UserToken: ${user.token}")
    }
    // New method for logout
    private fun logout() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Clear all saved user data from shared preferences
        editor.remove(USER_ID_KEY)
        editor.remove(USER_NAME_KEY)
        editor.remove(USER_EMAIL_KEY)
        editor.remove(USER_IMAGE_KEY)
        editor.remove(USER_TOKEN_KEY)

        // Apply changes
        editor.apply()

        // Redirect to the login screen or any other appropriate action
        val intent = Intent(this@LoginActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}