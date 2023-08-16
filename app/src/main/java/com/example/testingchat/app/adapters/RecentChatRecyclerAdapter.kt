package com.example.testingchat.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testingchat.R
import com.example.testingchat.model.ChatroomModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class RecentChatRecyclerAdapter(
    options: FirestoreRecyclerOptions<ChatroomModel?>?,
    context: Context
) :
    FirestoreRecyclerAdapter<ChatroomModel, RecentChatRecyclerAdapter.ChatroomModelViewHolder>(options!!) {
    var context: Context

    init {
        this.context = context
    }


    override fun onBindViewHolder(holder: ChatroomModelViewHolder, position: Int, model: ChatroomModel) {
        //TODO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatroomModelViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row, parent, false)
        return ChatroomModelViewHolder(view)
    }

    inner class ChatroomModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var usernameText: TextView
        var lastMessageText: TextView
        var lastMessageTime: TextView
        var profilePic: ImageView

        init {
            usernameText = itemView.findViewById(R.id.tv_username)
            lastMessageText = itemView.findViewById(R.id.tv_last_message)
            lastMessageTime = itemView.findViewById(R.id.tv_time)
            profilePic = itemView.findViewById(R.id.iv_profile_pic)
        }
    }
}