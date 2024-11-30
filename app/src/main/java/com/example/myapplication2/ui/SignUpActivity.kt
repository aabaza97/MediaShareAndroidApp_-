package com.example.myapplication2.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.myapplication2.R
import com.example.myapplication2.databinding.ActivitySignUpBinding
import com.example.myapplication2.service.RetrofitClient
import com.example.myapplication2.service.auth.TokenManager
import com.example.myapplication2.service.auth.UserInfoManager
import com.example.myapplication2.service.auth.repository.AuthRepository
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize dependencies
        val sharedPreferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        val tokenManager = TokenManager(sharedPreferences)
        val authService = RetrofitClient.authApi
        val userInfoManager = UserInfoManager(sharedPreferences)
        authRepository = AuthRepository(authService, tokenManager, userInfoManager)

        // Bind the layout to the binding variable
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        setupListeners()
    }

    private fun setupListeners() {
        binding.signUpButton.setOnClickListener {
            val fName = binding.firstNameEditText.text.toString()
            val lName = binding.lastNameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            // Clear error message
            binding.errorTextView.text = ""
            if (validateCredentials(fName, lName, email, password)) {
                // Perform sign up authentication
                performSignUp(fName, lName, email, password)
            } else {
                // Show error message
                binding.errorTextView.text = "Invalid credentials"
            }
        }
    }

    private fun performSignUp(fName: String, lName: String, email: String, password: String) {
        lifecycleScope.launch {
            try {
                // Disable the login button to prevent multiple requests
                binding.signUpButton.isEnabled = false
                binding.errorTextView.text = ""

                // Perform login request
                val response = authRepository.verifyEmail(email, password, fName, lName)
                if (response.isSuccess) {
                    navigateToOTP()
                } else {
                    // Show error message
                    binding.errorTextView.text = response.exceptionOrNull()?.message
                }
            } catch (e: Exception) {
                // Show error message
                binding.errorTextView.text = e.message
            } finally {
                // Enable the login button
                binding.signUpButton.isEnabled = true
            }
        }
    }

    private fun navigateToOTP() {
        // Start OTP Activity
        val intent = Intent(this, VerifyOTPActivity::class.java)
        intent.putExtra("email", binding.emailEditText.text.toString())
        startActivity(intent)
    }

    private fun validateCredentials(fName: String, lName: String, email: String, password: String): Boolean {
        // Some Validation Sign Up
        return fName.isNotEmpty() && lName.isNotEmpty() && email.isNotEmpty() && password.length >= 6
    }

}