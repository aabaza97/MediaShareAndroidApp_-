package com.example.myapplication2.ui
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myapplication2.R
import com.example.myapplication2.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind the layout to the binding variable
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (validateCredentials(email, password)) {
                // Perform login authentication
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
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

    private fun validateCredentials(email: String, password: String): Boolean {
        // Some Validation Login
        return email.isNotEmpty() && password.length >= 6
    }
}