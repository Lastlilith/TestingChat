package com.example.testingchat

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.testingchat.databinding.ActivityAuthPhoneBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class AuthPhoneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthPhoneBinding
    private val viewModel: AuthViewModel by viewModels()

    private var isUserExisting = false
    private val mAuth = FirebaseAuth.getInstance()
    private val timeoutSeconds = 60L

    private lateinit var verificationCode: String
    private lateinit var resendingToken: ForceResendingToken

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
                phoneNumber.error = "Wrong phone number"
                return@setOnClickListener
            } else {
                //send code from Firebase
                binding.btnSend.visibility = View.GONE
                binding.btnResendToken.visibility = View.VISIBLE
                sendOtp(countryCodePicker.fullNumberWithPlus, false)
            }
        }

        binding.btnEnter.setOnClickListener {
            Handler().postDelayed({
                if (isUserExisting) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Animatoo.animateSlideLeft(this@AuthPhoneActivity)
                    finish()
                } else if (viewModel.error.value?.isNotEmpty() == true) {
                    return@postDelayed
                } else {
                    val intent = Intent(this, RegisterUserActivity::class.java)
                    intent.putExtra("phoneNumber", countryCodePicker.fullNumberWithPlus)
                    startActivity(intent)
                    Animatoo.animateSlideLeft(this@AuthPhoneActivity)
                    finish()
                }
            }, 1000)
        }
    }

    private fun sendOtp(phoneNumber: String, isResend: Boolean) {
        setInProgress(true)
        val builder: PhoneAuthOptions.Builder = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .setActivity(this@AuthPhoneActivity)
            .setCallbacks(object : OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted( phoneAuthCredential: PhoneAuthCredential) {
                    signIn(phoneAuthCredential)
                    setInProgress(false)
                    binding.btnSend.visibility = View.GONE
                    binding.btnResendToken.visibility = View.VISIBLE
                }

                override fun onVerificationFailed( e: FirebaseException) {
                    Toast.makeText(applicationContext, "OTP verification failed", Toast.LENGTH_SHORT).show()
                    Log.e("POPO", "onVerificationFailed: ${e.message}, phoneNumber is $phoneNumber", )
                    setInProgress(false)
                    binding.btnSend.visibility = View.GONE
                    binding.btnResendToken.visibility = View.VISIBLE
                }

                override fun onCodeSent(
                    s: String,
                    forceResendingToken: ForceResendingToken
                ) {
                    super.onCodeSent(s, forceResendingToken)
                    verificationCode = s
                    resendingToken = forceResendingToken
                    Toast.makeText(applicationContext, "OTP sent successfully", Toast.LENGTH_SHORT).show()
                    setInProgress(false)
                }
            })
        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build())
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        }
    }

    private fun signIn(phoneAuthCredential: PhoneAuthCredential) {
        //login and go to next activity
    }

    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.authProgressBar.visibility = View.VISIBLE
            binding.btnSend.isEnabled = false
        } else {
            binding.authProgressBar.visibility = View.GONE
            binding.btnSend.isEnabled = true
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