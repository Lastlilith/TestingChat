package com.example.testingchat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testingchat.model.UserModel

class SearchUserAdapter(private val context: Context) : RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder>() {

    private var userList: List<UserModel> = emptyList()

    fun setContactList(users: List<UserModel>, currentUserId: Int) {
        userList = users.filter { it.id != currentUserId }
        notifyDataSetChanged()
    }

    inner class SearchUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.tv_username)
        private val phoneNumberTextView: TextView = itemView.findViewById(R.id.tv_phoneNumber)
        private val imageView: ImageView = itemView.findViewById(R.id.iv_profile_pic)

        fun bind(user: UserModel) {
            usernameTextView.text = user.username
            phoneNumberTextView.text = user.phone

//            Glide.with(itemView.context)
//                .load(contact.imageURL)
//                .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
//                .error(R.drawable.error_image) // Error image if loading fails
//                .into(imageView)
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

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

}