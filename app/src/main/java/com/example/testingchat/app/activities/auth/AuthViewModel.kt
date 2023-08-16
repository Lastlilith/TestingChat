package com.example.testingchat.app.activities.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
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
        viewModelScope.launch {
            try {
                val db = FirebaseFirestore.getInstance()
                val usersCollection = db.collection("users")

                val querySnapshot = withContext(Dispatchers.IO) {
                    usersCollection.whereEqualTo("phone", phoneNumber).get().await()
                }
                val userExists = !querySnapshot.isEmpty
                _userExistsLiveData.value = userExists
            } catch (e: Exception) {
                Log.e("POPO", "checkUserExistenceInFirestore: ${e.message}")
                _userExistsLiveData.value = false
            }
        }
    }


    fun signIn(phoneAuthCredential: PhoneAuthCredential) {
        viewModelScope.launch {
            try {
                val mAuth = FirebaseAuth.getInstance()
                val result = withContext(Dispatchers.IO) {
                    mAuth.signInWithCredential(phoneAuthCredential).await()
                }
                _signInLiveData.value = result?.user != null
            } catch (e: Exception) {
                Log.e("POPO", "signIn: ${e.message}")
                _signInLiveData.value = false
            }
        }
    }

    fun startResendTimer() {
        viewModelScope.launch {
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
                Log.e("POPO", "startResendTimer: ${e.message}")
            }
        }
    }
}