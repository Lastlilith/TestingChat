package com.example.testingchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testingchat.model.ChatroomModel
import com.example.testingchat.model.UserModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor() : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    // MutableLiveData to hold the user data
    private val _userData = MutableLiveData<UserModel>()
    val userData: LiveData<UserModel>
        get() = _userData

    private val _chatroomId = MutableLiveData<String>()
    val chatroomId: LiveData<String>
        get() = _chatroomId

    fun loadUserData(userId: String) {
        val userDocument = firestore.collection("users").document(userId)

        userDocument.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val user = snapshot.toObject<UserModel>()
                _userData.value = user!!
            }
        }
    }

    fun createOrLoadChatroom(currentUserId: String, otherUserId: String) {
        val chatroomId = getChatroomId(currentUserId, otherUserId)
        val chatroomRef = getChatroomReference(chatroomId)

        chatroomRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    // Chatroom exists, load it
                    _chatroomId.value = chatroomId
                } else {
                    // Chatroom doesn't exist, create it
                    val chatroomModel = ChatroomModel(
                        chatroomId,
                        listOf(currentUserId, otherUserId),
                        Timestamp.now(),
                        ""
                    )
                    chatroomRef.set(chatroomModel)
                        .addOnSuccessListener {
                            _chatroomId.value = chatroomId
                        }
                }
            }
        }
    }

    fun currentUserId(): String {
        return FirebaseAuth.getInstance().uid!!
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
}