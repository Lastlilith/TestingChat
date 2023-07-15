package com.example.testingchat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.testingchat.databinding.ActivityAuthPhoneBinding

class AuthPhoneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthPhoneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val countryCodePicker = binding.authCountryCode
        val phoneNumber = binding.etAuthMobileNumber

        countryCodePicker.registerCarrierNumberEditText(phoneNumber)
        binding.btnEnter.setOnClickListener {
            if (!countryCodePicker.isValidFullNumber) {
                phoneNumber.error = "Неверный формат номера телефона"
                return@setOnClickListener
            }
            val intent = Intent(this, AuthUsernameActivity::class.java)
            intent.putExtra("phone", countryCodePicker.fullNumberWithPlus)
            startActivity(intent)
            Animatoo.animateSlideLeft(this@AuthPhoneActivity)
            finish()
        }
    }
}