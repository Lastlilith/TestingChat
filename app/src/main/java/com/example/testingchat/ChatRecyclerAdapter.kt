package com.example.testingchat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testingchat.model.ChatMessageModel

class ChatRecyclerAdapter(private val context: Context): RecyclerView.Adapter<ChatRecyclerAdapter.ChatRecyclerViewHolder>() {

    private var messageList: List<ChatMessageModel> = emptyList()
    inner class ChatRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val leftChatLayout = itemView.findViewById<LinearLayout>(R.id.left_chat_layout)
        private val rightChatLayout = itemView.findViewById<LinearLayout>(R.id.right_chat_layout)
        private val leftChatTextView = itemView.findViewById<TextView>(R.id.tv_left_chat)
        private val rightChatTextView = itemView.findViewById<TextView>(R.id.tv_right_chat)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chat_message_recycler_row, parent, false)
        return ChatRecyclerViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: ChatRecyclerViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}