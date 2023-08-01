package com.example.testingchat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception
import java.util.Timer
import java.util.TimerTask


class AuthViewModel : ViewModel() {

    private val _userExistsLiveData = MutableLiveData<Boolean>()
    val userExistsLiveData: LiveData<Boolean>
        get() = _userExistsLiveData

    private val _signInLiveData = MutableLiveData<Boolean>()
    val signInLiveData: LiveData<Boolean>
        get() = _signInLiveData

    private val _countdownTimerLiveData = MutableLiveData<Long>()
    val countdownTimerLiveData: LiveData<Long>
        get() = _countdownTimerLiveData

    fun checkUserExistenceInFirestore(phoneNumber: String) {
        try {
            val db = FirebaseFirestore.getInstance()
            val usersCollection = db.collection("users")

            usersCollection
                .whereEqualTo("phone", phoneNumber)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val querySnapshot = task.result
                        val userExists = querySnapshot != null && !querySnapshot.isEmpty
                        _userExistsLiveData.value = userExists
                    } else {
                        // Error occurred while querying Firestore
                        _userExistsLiveData.value = false
                    }
                }
        } catch (e: Exception) {
            Log.e("POPO", "checkUserExistenceInFirestore: ${e.message}", )
        }

    }

    fun signIn(phoneAuthCredential: PhoneAuthCredential) {
        try {
            val mAuth = FirebaseAuth.getInstance()
            mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener { task ->
                    _signInLiveData.value = task.isSuccessful
                }
        } catch (e: Exception) {
            Log.e("POPO", "signIn: ${e.message}")
        }

    }

    fun startResendTimer() {
        try {
            _countdownTimerLiveData.value = 15L // Initial value
            val timer = Timer()
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val currentValue = _countdownTimerLiveData.value ?: 0L
                    val newValue = currentValue - 1
                    _countdownTimerLiveData.postValue(newValue)
                    if (newValue <= 0) {
                        timer.cancel()
                    }
                }
            }, 0, 1000)
        } catch (e: Exception) {
            Log.e("POPO", "startResendTimer: ${e.message}", )
        }

    }
}