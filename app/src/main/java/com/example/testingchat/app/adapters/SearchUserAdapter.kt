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
import com.example.testingchat.app.activities.chat.ChatActivity
import com.example.testingchat.R
import com.example.testingchat.model.UserModel
import com.example.testingchat.utils.AndroidUtil
import com.example.testingchat.utils.FirebaseUtil

class SearchUserAdapter(private val context: Context) : RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder>() {

    private var userList: List<UserModel> = emptyList()

    fun setContactList(users: List<UserModel>, currentUserId: String) {
        userList = users.filter { it.id != currentUserId }
        notifyDataSetChanged()
    }

    inner class SearchUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.tv_username)
        private val phoneNumberTextView: TextView = itemView.findViewById(R.id.tv_phoneNumber)
        var imageView: ImageView = itemView.findViewById(R.id.iv_profile_pic)

        fun bind(user: UserModel) {
            usernameTextView.text = user.username
            phoneNumberTextView.text = user.phone
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.search_user_recycler_row, parent, false)
        return SearchUserViewHolder(itemView)
    }

    override fun getItemCount() = userList.size

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)

        FirebaseUtil.getOtherProfilePicStorageRef(user.id)
            .downloadUrl
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val uri: Uri = it.result
                    AndroidUtil.setProfileImage(context, uri, holder.imageView)
                }
            }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userId", user.id.toString())
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

}