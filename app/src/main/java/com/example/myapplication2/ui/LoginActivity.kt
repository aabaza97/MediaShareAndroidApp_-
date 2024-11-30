package com.example.myapplication2.ui
import android.content.Intent
import android.os.Bundle
import android.util.Patterns.EMAIL_ADDRESS
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.myapplication2.R
import com.example.myapplication2.databinding.ActivityLoginBinding
import com.example.myapplication2.service.RetrofitClient
import com.example.myapplication2.service.auth.TokenManager
import com.example.myapplication2.service.auth.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var authRepository: AuthRepository

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize dependencies
        val sharedPreferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        val tokenManager = TokenManager(sharedPreferences)
        val authService = RetrofitClient.authApi
        authRepository = AuthRepository(authService, tokenManager)

        // Bind the layout to the binding variable
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // Check if user is already logged in
        if (authRepository.isUserLoggedIn()) {
            navigateToHome()
            return
        }

        setupListeners()
    }

    private
    fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private
    fun setupListeners() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (validateCredentials(email, password)) {
               performLogin(email, password)
            } else {
                // Show error message
                binding.errorTextView.text = "Invalid credentials"
            }
        }

        binding.signUpTextView.setOnClickListener {
            // TODO: Implement Navigation to SignUpActivity
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private
    fun performLogin(email: String, password: String) {
        lifecycleScope.launch {
            try {
                // Disable the login button to prevent multiple requests
                binding.loginButton.isEnabled = false
                binding.errorTextView.text = ""

                // Perform login request
                val response = authRepository.login(email, password)
                if (response.isSuccess) {
                    navigateToHome()
                } else {
                    // Show error message
                    binding.errorTextView.text = response.exceptionOrNull()?.message
                }
            } catch (e: Exception) {
                // Show error message
                binding.errorTextView.text = e.message
            } finally {
                // Enable the login button
                binding.loginButton.isEnabled = true
            }
        }
    }

    private
    fun validateCredentials(email: String, password: String): Boolean {
        // Some Validation Login
        return email.isNotEmpty() && EMAIL_ADDRESS.matcher(email).matches() && password.length >= 6
    }
}