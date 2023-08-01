package com.example.testingchat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.testingchat.databinding.ActivityAuthPhoneBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class AuthPhoneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthPhoneBinding
    private val viewModel: AuthViewModel by viewModels()

    private var isUserExisting = false
    private val mAuth = FirebaseAuth.getInstance()
    private var timeoutSeconds = 15L

    private lateinit var countryCodePicker: CountryCodePicker

    private lateinit var verificationCode: String
    private lateinit var resendingToken: ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()

        countryCodePicker = binding.authCountryCode
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
            val enteredOtp: String = binding.etSmsCode.text.toString()
            if (binding.etSmsCode.text.isEmpty()) {
                binding.etSmsCode.error = "Enter valid code"
            } else {
                val credential: PhoneAuthCredential =
                    PhoneAuthProvider.getCredential(verificationCode, enteredOtp)
                signIn(credential)
                setInProgress(true)
            }
        }

        binding.btnResendToken.setOnClickListener {
            sendOtp(countryCodePicker.fullNumberWithPlus, true)
        }
    }

    private fun sendOtp(phoneNumber: String, isResend: Boolean) {
        startResendTimer()
        setInProgress(true)
        val builder: PhoneAuthOptions.Builder = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .setActivity(this@AuthPhoneActivity)
            .setCallbacks(object : OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signIn(phoneAuthCredential)
                    setInProgress(false)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(
                        applicationContext,
                        "OTP verification failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("POPO", "onVerificationFailed: ${e.message}, phoneNumber is $phoneNumber")
                    setInProgress(false)
                }

                override fun onCodeSent(
                    s: String,
                    forceResendingToken: ForceResendingToken
                ) {
                    super.onCodeSent(s, forceResendingToken)
                    verificationCode = s
                    resendingToken = forceResendingToken
                    Toast.makeText(applicationContext, "OTP sent successfully", Toast.LENGTH_SHORT)
                        .show()
                    setInProgress(false)
                }
            })
        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(
                builder.setForceResendingToken(resendingToken).build()
            )
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        }
    }

    private fun signIn(phoneAuthCredential: PhoneAuthCredential) {
        //login and go to next activity
        setInProgress(true)
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener { task ->
            setInProgress(false)
            if (task.isSuccessful) {
                val intent = Intent(this@AuthPhoneActivity, RegisterUserActivity::class.java)
                intent.putExtra("phone", countryCodePicker.fullNumberWithPlus)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "OTP verification failed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.authProgressBar.visibility = View.VISIBLE
            binding.btnSend.isEnabled = false
            binding.btnEnter.isEnabled = false
            binding.btnResendToken.isEnabled = false
        } else {
            binding.authProgressBar.visibility = View.GONE
            binding.btnSend.isEnabled = true
            binding.btnEnter.isEnabled = true
        }
    }

    private fun startResendTimer() {
        binding.btnResendToken.isEnabled = false
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                timeoutSeconds--
                binding.btnResendToken.text = "Resend OTP in $timeoutSeconds seconds"
                if (timeoutSeconds <= 0) {
                    timeoutSeconds = 15L
                    timer.cancel()
                    runOnUiThread { binding.btnResendToken.isEnabled = true }
                }
            }
        }, 0, 1000)
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