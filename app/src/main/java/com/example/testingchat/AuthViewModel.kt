package com.example.testingchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore


class AuthViewModel : ViewModel() {

    private val _userExistsLiveData = MutableLiveData<Boolean>()
    val userExistsLiveData: LiveData<Boolean>
        get() = _userExistsLiveData

    fun checkUserExistenceInFirestore(phoneNumber: String) {
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
    }
}