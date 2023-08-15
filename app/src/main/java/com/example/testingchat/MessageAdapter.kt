package com.example.testingchat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testingchat.model.ChatMessageModel
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val messageList: ArrayList<ChatMessageModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVED = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            //inflate receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.receiver_layout, parent, false)
            ReceiveViewHolder(view)
        } else {
            // inflate sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false)
            SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if (holder.javaClass == SentViewHolder::class.java) {
            val viewHolder = holder as SentViewHolder
            viewHolder.sentMessage.text = currentMessage.message
            adjustTextViewWidth(viewHolder.sentMessage, currentMessage.message)
        } else {
            val viewHolder = holder as ReceiveViewHolder
            viewHolder.receiveMessage.text = currentMessage.message
            adjustTextViewWidth(viewHolder.receiveMessage, currentMessage.message)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if (FirebaseAuth.getInstance().currentUser?.uid == currentMessage.senderId) {
            ITEM_SENT
        } else {
            ITEM_RECEIVED
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    inner class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage: TextView = itemView.findViewById(R.id.tv_sent_message)
    }

    inner class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage: TextView = itemView.findViewById(R.id.tv_received_message)
    }

    private fun adjustTextViewWidth(textView: TextView, text: String) {
        // Get the screen width
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        // Calculate the desired maximum width (75% of the screen width)
        val maxWidth = (screenWidth * 0.70).toInt()

        // Measure the width of the text
        textView.text = text
        textView.measure(0, 0)
        val textWidth = textView.measuredWidth

        // Set the width of the TextView based on content length and maxWidth
        val layoutParams = textView.layoutParams
        layoutParams.width = if (textWidth > maxWidth) maxWidth else textWidth
        textView.layoutParams = layoutParams
    }
}