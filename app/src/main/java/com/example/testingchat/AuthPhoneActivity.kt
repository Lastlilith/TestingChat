package com.example.testingchat

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.testingchat.databinding.ActivityAuthPhoneBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthPhoneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthPhoneBinding
    private val viewModel: AuthViewModel by viewModels()

    private var isUserExisting = false

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
            viewModel.sendAuthRequest(countryCodePicker.fullNumberWithPlus)
        }

        binding.btnEnter.setOnClickListener {
            viewModel.validateUser(binding.etSmsCode.text.toString())
            Handler().postDelayed({
                if (isUserExisting) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else if (viewModel.error.value?.isNotEmpty() == true) {
                    return@postDelayed
                } else {
                    val intent = Intent(this, AuthUsernameActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }, 1000)
        }
    }

    private fun observeViewModel() {
        viewModel.apply {
            phoneText.observe(this@AuthPhoneActivity) {
                binding.etAuthMobileNumber.text.toString()
            }
            progress.observe(this@AuthPhoneActivity) {
                if (it) {
                    binding.authProgressBar.visibility = View.VISIBLE
                    binding.btnSend.isEnabled = false
                } else {
                    binding.authProgressBar.visibility = View.GONE
                    binding.btnSend.isEnabled = true
                }
            }
            error.observe(this@AuthPhoneActivity) {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
            successCode.observe(this@AuthPhoneActivity) {
                binding.btnEnter.isEnabled = it
            }
            smsText.observe(this@AuthPhoneActivity) {
                binding.etSmsCode.text.toString()
            }
            isUserExist.observe(this@AuthPhoneActivity) {
                isUserExisting = it
            }
            viewModel.isUserExist.observe(this@AuthPhoneActivity) {
                isUserExisting = it
            }
        }

    }
}