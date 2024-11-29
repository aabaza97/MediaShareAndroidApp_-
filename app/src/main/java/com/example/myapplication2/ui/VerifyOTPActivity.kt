package com.example.myapplication2.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication2.databinding.ActivityVerifyOtpactivityBinding

class VerifyOTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyOtpactivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        if (enteredOtp.length == 6) {
            // Verify OTP
            // Populate user model with data
            // Finish and go to the next activity
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        } else {
            // Show an error message
            Toast.makeText(this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show()
        }

    }

}