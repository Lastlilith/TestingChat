package com.example.testingchat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testingchat.R
import com.example.testingchat.model.UserModel

class RecentChatsAdapter(private val context: Context) : RecyclerView.Adapter<RecentChatsAdapter.RecentChatsViewHolder>() {

    private var userList: List<UserModel> = emptyList()

    inner class RecentChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.tv_username)
        private val lastMessageText: TextView = itemView.findViewById(R.id.tv_last_message)
        private val imageView: ImageView = itemView.findViewById(R.id.iv_profile_pic)
        private val lastMessageTime: TextView = itemView.findViewById(R.id.tv_time)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentChatsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recent_chat_recycler_row, parent, false)
        return RecentChatsViewHolder(itemView)
    }

    override fun getItemCount() = userList.size

    override fun onBindViewHolder(holder: RecentChatsViewHolder, position: Int) {
        //TODO
    }

}