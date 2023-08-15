package com.example.testingchat.model

import com.google.firebase.Timestamp

data class ChatMessageModel(
    val message: String = "",
    val senderId: String = "",
    val timeStamp: Timestamp = Timestamp.now()
)

