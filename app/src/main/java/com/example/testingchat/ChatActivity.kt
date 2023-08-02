package com.example.testingchat

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.testingchat.databinding.ActivityChatBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    private lateinit var userId: String
    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatRoomViewModel by viewModels()
    private lateinit var chatroomId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("userId") ?: ""
        observeViewModel()

//        chatroomId = getChatroomId(currentUserId(), userId)

        viewModel.loadUserData(userId)
        viewModel.createOrLoadChatroom(viewModel.currentUserId(), userId)
        binding.btnBack.setOnClickListener { finish() }
        Log.e("POPO", "onCreate: $userId")

        binding.btnSendMessage.setOnClickListener {
            binding.etMessage.text = null
        }
    }

    private fun observeViewModel() {
        viewModel.userData.observe(this) { user ->
            if (user != null) {
                binding.toolbarTUsername.text = user.username
            }
        }
        viewModel.chatroomId.observe(this) { chatroomId ->
            // Use the chatroomId, e.g., for setting up the chat UI
            this.chatroomId = chatroomId
            Log.e("ChatActivity", "Received chatroomId: $chatroomId")
        }
    }
}