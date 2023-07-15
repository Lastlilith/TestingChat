package com.example.testingchat

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.testingchat.databinding.ActivityAuthPhoneBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthPhoneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthPhoneBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()

        val countryCodePicker = binding.authCountryCode
        val phoneNumber = binding.etAuthMobileNumber

        countryCodePicker.registerCarrierNumberEditText(phoneNumber)
        binding.btnSend.setOnClickListener {
            if (!countryCodePicker.isValidFullNumber) {
                phoneNumber.error = "Неверный формат номера телефона"
                return@setOnClickListener
            }
            viewModel.sendAuthRequest()

//            val intent = Intent(this, AuthUsernameActivity::class.java)
//            intent.putExtra("phone", countryCodePicker.fullNumberWithPlus)
//            startActivity(intent)
//            Animatoo.animateSlideLeft(this@AuthPhoneActivity)
//            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.phoneText.observe(this) {
            binding.etAuthMobileNumber.text
        }
        viewModel.progress.observe(this) {
            if (it) {
                binding.authProgressBar.visibility = View.VISIBLE
                binding.btnSend.isEnabled = false
            } else {
                binding.authProgressBar.visibility = View.GONE
                binding.btnSend.isEnabled = true
            }
        }
        viewModel.error.observe(this) {
            Snackbar.make(binding.root,  it, Snackbar.LENGTH_LONG).show()
            binding.btnEnter.isEnabled = false
        }
        viewModel.successCode.observe(this) {
            binding.btnEnter.isEnabled = it
        }
    }
}