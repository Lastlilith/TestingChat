package com.example.testingchat

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testingchat.base.BaseViewModel
import com.example.testingchat.model.UserModel
import com.google.firebase.firestore.FieldValue
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

                val chatRoomId = createChatRoom(listOf(123, 456), "senderId")

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

    private suspend fun createChatRoom(userIds: List<Int>, senderId: String): String {
        return withContext(Dispatchers.IO) {
            val firestore = FirebaseFirestore.getInstance()
            val chatRoomsCollection = firestore.collection("chatRooms")

            // Create a new document with an automatically generated ID
            val newChatRoomRef = chatRoomsCollection.document()

            // Create a data map for the chat room
            val chatRoomData = hashMapOf(
                "chatroomId" to newChatRoomRef.id,
                "userIds" to userIds,
                "lastMessageTimeStamp" to FieldValue.serverTimestamp(),
                "lastMessageSenderId" to senderId
            )

            // Set the data for the new chat room document
            newChatRoomRef.set(chatRoomData).await()

            // Return the ID of the created chat room
            return@withContext newChatRoomRef.id
        }
    }
}