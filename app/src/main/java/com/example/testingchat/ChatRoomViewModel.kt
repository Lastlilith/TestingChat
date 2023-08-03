package com.example.testingchat

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testingchat.base.BaseViewModel
import com.example.testingchat.model.ChatMessageModel
import com.example.testingchat.model.ChatroomModel
import com.example.testingchat.model.UserModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor() : BaseViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    // MutableLiveData to hold the user data
    private val _userData = MutableLiveData<UserModel>()
    val userData: LiveData<UserModel>
        get() = _userData

    private val _chatroomId = MutableLiveData<String>()
    val chatroomId: LiveData<String>
        get() = _chatroomId

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    private val _chatroomModel = MutableLiveData<ChatroomModel>()
    val chatroomModel: LiveData<ChatroomModel>
        get() = _chatroomModel


    fun loadUserData(userId: String) {
        viewModelScope.launch {
            try {
                val userDocument = firestore.collection("users").document(userId).get().await()

                if (userDocument.exists()) {
                    val user = userDocument.toObject<UserModel>()
                    _userData.value = user!!
                } else {
                    // Handle not found
                    Toast.makeText(application.applicationContext, "Try again later", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Handle error
                Log.e("POPO", "loadUserData: ${e.message}", )
                Toast.makeText(application.applicationContext, "Try again later", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createOrLoadChatroom(currentUserId: String, otherUserId: String) {
        viewModelScope.launch {
            val chatroomId = getChatroomId(currentUserId, otherUserId)
            val chatroomRef = getChatroomReference(chatroomId)

            try {
                val document = chatroomRef.get().await()
                if (document.exists()) {
                    // Chatroom exists, load it
                    _chatroomId.value = chatroomId
                } else {
                    // Chatroom doesn't exist, create it
                    val chatroomModel = ChatroomModel(
                        chatroomId,
                        listOf(currentUserId, otherUserId),
                        Timestamp.now(),
                        "",
                        ""
                    )
                    chatroomRef.set(chatroomModel).await()
                    _chatroomId.value = chatroomId
                }
            } catch (e: Exception) {
                // Handle error
                Log.e("POPO", "createOrLoadChatroom: ${e.message}", )
            }
        }
    }

    fun currentUserId(): String {
        return FirebaseAuth.getInstance().uid!!
    }

    fun sendMessageToUser(message: String) {
        val chatMessageModel = ChatMessageModel(message, currentUserId(), Timestamp.now())
        // Check if chatroomId is not null before proceeding
        val actualChatroomId = _chatroomId.value
        if (actualChatroomId != null) {
            val chatroomModel = _chatroomModel.value // Get the chatroomModel from LiveData
            chatroomModel?.lastMessageTimestamp = Timestamp.now()
            chatroomModel?.lastMessageSenderId = currentUserId()
            chatroomModel?.lastMessage = message

            // Update the chatroomModel in Firestore
            val chatroomRef = getChatroomReference(actualChatroomId)
            if (chatroomModel != null) {
                chatroomRef.set(chatroomModel)
            }

            // Add the chat message to the "chats" collection inside the chatroom
            val chatMessageRef = getChatroomMessageReference(actualChatroomId)
            chatMessageRef.add(chatMessageModel)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _message.value = message
                    }
                }
        }
    }

    private fun getChatroomId(userId1: String, userId2: String): String {
        return if (userId1 < userId2) {
            userId1 + "_" + userId2
        } else {
            userId2 + "_" + userId1
        }
    }

    private fun getChatroomReference(chatroomId: String): DocumentReference {
        return firestore.collection("chatrooms").document(chatroomId)
    }

    private fun getChatroomMessageReference(chatroomId: String): CollectionReference {
        return getChatroomReference(chatroomId).collection("chats")
    }


}