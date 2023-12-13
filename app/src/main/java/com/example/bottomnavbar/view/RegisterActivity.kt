package com.example.bottomnavbar.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.bottomnavbar.R
import com.example.bottomnavbar.api.Login
import com.example.bottomnavbar.api.RetrofitImp
import com.example.bottomnavbar.databinding.ActivityRegisterBinding
import com.example.bottomnavbar.model.User
import com.example.bottomnavbar.model.User2
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Get the root view to be used as the parent for Snackbar
        val rootView = window.decorView.rootView
        binding.btnReturn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnTermsAndPolicy.setOnClickListener {
            startActivity(Intent(this, RegisterdoctorActivity::class.java))
        }

        // Initialize the TextWatcher for the input fields
        initTextWatchers()
        binding.btnSignUp.setOnClickListener {
            /*if (validateForm()) {
                // If validation is successful, navigate to the LoginActivity.
                registerUser()
            } else {
                // If validation fails, show a Snackbar with an error message.
                Snackbar.make(rootView, getString(R.string.msg_error_inputs), Snackbar.LENGTH_SHORT).show()
            }*/  registerUser()
        }

    }
    private fun initTextWatchers() {
        // TextWatcher for Full Name input
        binding.tiFullName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateFullName()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // TextWatcher for Email input
        binding.tiEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePhoneNumber()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // TextWatcher for Password input
        binding.tiPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // TextWatcher for Confirm Password input
        binding.tiConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateConfirmPassword()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun validateFullName(): Boolean {
        val fullName = binding.tiFullName.text.toString()
        binding.tiFullNameLayout.error = null

        if (fullName.isEmpty()) {
            binding.tiFullNameLayout.error = getString(R.string.msg_must_not_be_empty)
            return false
        }

        if (fullName.length < 6) {
            binding.tiFullNameLayout.error = getString(R.string.msg_check_your_characters)
            return false
        }

        return true
    }
    private fun validatePhoneNumber(): Boolean {
        val phoneNumber = binding.tiEmail.text.toString()
        binding.tiEmailLayout.error = null

        if (phoneNumber.isEmpty()) {
            binding.tiEmailLayout.error = getString(R.string.msg_must_not_be_empty)
            return false
        }

        // Vous pouvez ajuster cette expression régulière en fonction des critères spécifiques de votre application
        val phoneRegex = Regex("^[0-9]{8}$")

        if (!phoneRegex.matches(phoneNumber)) {
            binding.tiEmailLayout.error = getString(R.string.msg_check_your_phone_number)
            return false
        }

        return true
    }
    private fun validatePassword(): Boolean {
        val password = binding.tiPassword.text.toString()
        binding.tiPasswordLayout.error = null

        if (password.isEmpty()) {
            binding.tiPasswordLayout.error = getString(R.string.msg_must_not_be_empty)
            return false
        }

        if (password.length < 6) {
            binding.tiPasswordLayout.error = getString(R.string.msg_check_your_characters)
            return false
        }

        return true
    }

    private fun validateConfirmPassword(): Boolean {
        val confirmPassword = binding.tiConfirmPassword.text.toString()
        val password = binding.tiPassword.text.toString()
        binding.tiConfirmPasswordLayout.error = null
        binding.tiPasswordLayout.error = null

        if (confirmPassword.isEmpty()) {
            binding.tiConfirmPasswordLayout.error = getString(R.string.msg_must_not_be_empty)
            return false
        }

        if (confirmPassword.length < 6) {
            binding.tiConfirmPasswordLayout.error = getString(R.string.msg_check_your_characters)
            return false
        }

        if (confirmPassword != password) {
            binding.tiConfirmPasswordLayout.error = getString(R.string.msg_check_your_confirm_Password)
            binding.tiPasswordLayout.error = getString(R.string.msg_check_your_confirm_Password)
            return false
        }

        return true
    }


    private fun validateForm(): Boolean {
        return validateFullName() && validatePhoneNumber() && validatePassword() && validateConfirmPassword()
    }

    private fun registerUser() {
        val user2 = User2(
            numTel = binding.tiEmail.text.toString(),
            password = binding.tiPassword.text.toString(),
            name = binding.tiFullName.text.toString(),

            )

        val registerService = RetrofitImp.buildRetrofit().create(Login::class.java)
        registerService.register(user2).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    handleSuccessfulRegistration(response.body())
                } else {
                    handleRegistrationFailure(response.errorBody()?.string().toString())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                handleRegistrationFailure(t.message.toString())
            }
        })
    }
    private fun handleSuccessfulRegistration(user: User?) {
        // Handle successful registration
        // Optionally, you can navigate to the login screen
        startActivity(Intent(this@Register, LoginActivity::class.java))
        finish()
    }

    private fun handleRegistrationFailure(errorMessage: String) {
        // Handle registration failure
        showSnackbar(errorMessage)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }



}