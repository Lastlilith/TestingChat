package com.example.testingchat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.testingchat.databinding.ActivityAuthPhoneBinding
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

    private val mAuth = FirebaseAuth.getInstance()
    private var timeoutSeconds = 15L

    private lateinit var countryCodePicker: CountryCodePicker

    private lateinit var verificationCode: String
    private lateinit var resendingToken: ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        countryCodePicker = binding.authCountryCode
        val phoneNumber = binding.etAuthMobileNumber

        countryCodePicker.registerCarrierNumberEditText(phoneNumber)


        observeUser()


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
                viewModel.signIn(credential)
                setInProgress(true)
            }
        }

        binding.btnResendToken.setOnClickListener {
            if (!countryCodePicker.isValidFullNumber) {
                phoneNumber.error = "Wrong phone number"
                return@setOnClickListener
            } else {
                sendOtp(countryCodePicker.fullNumberWithPlus, true)
            }

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
                    viewModel.signIn(phoneAuthCredential)
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

    private fun observeUser() {
        viewModel.userExistsLiveData.observe(this@AuthPhoneActivity) {
            if (it) {
                val intent = Intent(this@AuthPhoneActivity, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@AuthPhoneActivity, RegisterUserActivity::class.java)
                intent.putExtra("phone", countryCodePicker.fullNumberWithPlus)
                startActivity(intent)
            }
        }
        viewModel.signInLiveData.observe(this) { signInSuccessful ->
            if (signInSuccessful) {
                // Sign-in successful, check user existence in Firestore
                val phoneNumber = countryCodePicker.fullNumberWithPlus
                viewModel.checkUserExistenceInFirestore(phoneNumber)
            } else {
                // Sign-in failed, show a message to the user
                Toast.makeText(
                    applicationContext,
                    "OTP verification failed",
                    Toast.LENGTH_SHORT
                ).show()
                setInProgress(false)
            }
        }
    }
}