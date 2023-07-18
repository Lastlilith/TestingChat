package com.example.testingchat

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testingchat.base.BaseViewModel
import com.example.testingchat.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : BaseViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    fun loadUserNameFromFirestore(userId: String) {
        viewModelScope.launch {
            try {
                val user = getUserFromFirestore(userId)
                _username.value = user?.name ?: ""
            } catch (exception: Exception) {
                Toast.makeText(application.applicationContext, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun getUserFromFirestore(userId: String): UserModel? {
        return withContext(Dispatchers.IO) {
            val firestore = FirebaseFirestore.getInstance()
            val userDocRef = firestore.collection("users").document(userId).get().await()
            return@withContext userDocRef.toObject(UserModel::class.java)
        }
    }
}