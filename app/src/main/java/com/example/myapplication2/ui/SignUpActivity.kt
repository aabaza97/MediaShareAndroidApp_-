package com.example.myapplication2.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myapplication2.R
import com.example.myapplication2.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind the layout to the binding variable
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        binding.signUpButton.setOnClickListener {
            val fName = binding.firstNameEditText.text.toString()
            val lName = binding.lastNameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            // Clear error message
            binding.errorTextView.text = ""
            if (validateCredentials(fName, lName, email, password)) {
                // Perform sign up authentication
                // startActivity(Intent(this, com.example.myapplication2.ui.HomeActivity::class.java))
                // finish()
            } else {
                // Show error message
                binding.errorTextView.text = "Invalid credentials"
            }
        }
    }

    private fun validateCredentials(fName: String, lName: String, email: String, password: String): Boolean {
        // Some Validation Sign Up
        return fName.isNotEmpty() && lName.isNotEmpty() && email.isNotEmpty() && password.length >= 6
    }

}