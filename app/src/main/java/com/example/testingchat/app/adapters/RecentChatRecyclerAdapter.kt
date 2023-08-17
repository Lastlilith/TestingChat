package com.example.testingchat.app.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testingchat.R
import com.example.testingchat.app.activities.chat.ChatActivity
import com.example.testingchat.model.ChatroomModel
import com.example.testingchat.model.UserModel
import com.example.testingchat.utils.AndroidUtil
import com.example.testingchat.utils.FirebaseUtil
import com.example.testingchat.utils.FirebaseUtil.currentUserId
import com.example.testingchat.utils.FirebaseUtil.getOtherUserFromChatroom
import com.example.testingchat.utils.FirebaseUtil.timestampToString
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot


class RecentChatRecyclerAdapter(
    options: FirestoreRecyclerOptions<ChatroomModel?>?,
    context: Context
) :
    FirestoreRecyclerAdapter<ChatroomModel, RecentChatRecyclerAdapter.ChatroomModelViewHolder>(options!!) {
    var context: Context

    init {
        this.context = context
    }


    override fun onBindViewHolder(holder: ChatroomModelViewHolder, position: Int, model: ChatroomModel
    ) {
        getOtherUserFromChatroom(model.userIds)
            .get().addOnCompleteListener { task: Task<DocumentSnapshot> ->
                if (task.isSuccessful) {
                    val lastMessageSentByMe = model.lastMessageSenderId == currentUserId()
                    val otherUserModel = task.result.toObject(UserModel::class.java)
                    FirebaseUtil.getOtherProfilePicStorageRef(otherUserModel!!.id)
                        .downloadUrl
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val uri: Uri = it.result
                                AndroidUtil.setProfileImage(context, uri, holder.profilePic)
                            }
                        }
                    holder.usernameText.text = otherUserModel.username
                    if (lastMessageSentByMe) {
                        holder.lastMessageText.text = "Me : " + model.lastMessage
                    } else {
                        holder.lastMessageText.text = model.lastMessage
                    }
                    holder.lastMessageTime.text = timestampToString(model.lastMessageTimestamp)
                    holder.itemView.setOnClickListener {
                        //navigate to chat activity
                        val intent = Intent(context, ChatActivity::class.java)
                        AndroidUtil.passUserModelAsIntent(intent, otherUserModel)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    }
                }
            }
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