package com.example.testingchat

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testingchat.databinding.ActivityChatBinding
import com.example.testingchat.model.ChatMessageModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    private lateinit var userId: String
    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatRoomViewModel by viewModels()
    private lateinit var chatroomId: String
    private lateinit var adapter: MessageAdapter
    private lateinit var chatRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("userId") ?: ""
        observeViewModel()

        chatRecyclerView = binding.rvChatMessages
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MessageAdapter(this, ArrayList())
        chatRecyclerView.adapter = adapter

        viewModel.loadUserData(userId)
        viewModel.createOrLoadChatroom(viewModel.currentUserId(), userId)
        binding.btnBack.setOnClickListener { finish() }
        Log.e("POPO", "onCreate: $userId")

        binding.btnSendMessage.setOnClickListener {
            val messageText = binding.etMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                viewModel.sendMessageToUser(messageText)
                binding.etMessage.text.clear()
            } else {
                binding.etMessage.error = "Please enter a message"
                return@setOnClickListener
            }
        }
    }


    private fun observeViewModel() {
        viewModel.userData.observe(this) { user ->
            if (user != null) {
                binding.toolbarTUsername.text = user.username
            }
        }
        viewModel.chatroomId.observe(this) { chatroomId ->
            this.chatroomId = chatroomId
            Log.e("ChatActivity", "Received chatroomId: $chatroomId")
            viewModel.loadChatMessages(chatroomId)
        }
        viewModel.message.observe(this) {
            binding.etMessage.text.toString()
        }
        viewModel.chatMessages.observe(this) { messages ->
            adapter.messageList.clear()
            adapter.messageList.addAll(messages)
            adapter.notifyDataSetChanged()
        }
    }
}