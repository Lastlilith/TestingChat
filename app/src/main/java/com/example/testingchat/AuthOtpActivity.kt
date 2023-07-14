package com.example.testingchat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testingchat.databinding.ActivityAuthOtpBinding

class AuthOtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthOtpBinding

    private var phoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        phoneNumber = intent.getStringExtra("phone")
        Toast.makeText(this, "$phoneNumber", Toast.LENGTH_SHORT).show()
    }
}