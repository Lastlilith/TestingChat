package com.example.testingchat.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatroomModel(
    val chatroomId: String = "",
    val userIds: List<String> = emptyList(),
    var lastMessageTimestamp: Timestamp = Timestamp.now(),
    var lastMessageSenderId: String = "",
    var lastMessage: String = ""
) : Parcelable
