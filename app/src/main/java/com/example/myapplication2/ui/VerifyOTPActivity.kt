package com.example.myapplication2.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication2.databinding.ActivityVerifyOtpactivityBinding
import com.example.myapplication2.service.RetrofitClient
import com.example.myapplication2.service.auth.TokenManager
import com.example.myapplication2.service.auth.repository.AuthRepository
import kotlinx.coroutines.launch

class VerifyOTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyOtpactivityBinding
    private lateinit var authRepository: AuthRepository
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize dependencies
        val sharedPreferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        val tokenManager = TokenManager(sharedPreferences)
        val authService = RetrofitClient.authApi
        authRepository = AuthRepository(authService, tokenManager)

        // Get the email from the intent
        email = intent.getStringExtra("email") ?: ""

        binding = ActivityVerifyOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup OTP fields
        setupListeners()
        setupOTPFields()
    }

    private fun setupListeners() {
        // Verify button
        binding.verifyButton.setOnClickListener {
            verifyOTP()
        }
    }

    private fun setupOTPFields() {
        val otpEditTexts = arrayOf(
            binding.otpEditText1,
            binding.otpEditText2,
            binding.otpEditText3,
            binding.otpEditText4,
            binding.otpEditText5,
            binding.otpEditText6
        )

        for (i in 0 until otpEditTexts.size - 1) {
            otpEditTexts[i].addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        otpEditTexts[i + 1].requestFocus()
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }

    }

    private fun verifyOTP() {
        // Collect OTP from input fields
        val enteredOtp = binding.otpEditText1.text.toString() +
                binding.otpEditText2.text.toString() +
                binding.otpEditText3.text.toString() +
                binding.otpEditText4.text.toString() +
                binding.otpEditText5.text.toString() +
                binding.otpEditText6.text.toString()

        if (enteredOtp.length == 6 && email.isNotEmpty()) {
            performOTPVerification(enteredOtp)
        } else {
            // Show an error message
            Toast.makeText(this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show()
        }

    }

    private fun performOTPVerification(enteredOtp: String) {
        lifecycleScope.launch {
            try {
                // Disable the login button to prevent multiple requests
                binding.verifyButton.isEnabled = false

                // Perform login request
                val response = authRepository.register(email, enteredOtp)
                if (response.isSuccess) {
                    navigateToHome()
                } else {
                    // Show error message
                    Toast.makeText(this@VerifyOTPActivity, response.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Show error message
                Toast.makeText(this@VerifyOTPActivity, e.message, Toast.LENGTH_SHORT).show()
            } finally {
                // Enable the login button
                binding.verifyButton.isEnabled = true
            }
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

}