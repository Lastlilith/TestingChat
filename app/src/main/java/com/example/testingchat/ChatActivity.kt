package com.example.testingchat

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.testingchat.databinding.ActivityChatBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    private lateinit var userId: String
    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatRoomViewModel by viewModels()
    private lateinit var chatroomId: String
    private lateinit var chatRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("userId") ?: ""
        observeViewModel()

        chatRecyclerView = binding.rvChatMessages


        viewModel.loadUserData(userId)
        viewModel.createOrLoadChatroom(viewModel.currentUserId(), userId)
        binding.btnBack.setOnClickListener { finish() }
        Log.e("POPO", "onCreate: $userId")

        binding.btnSendMessage.setOnClickListener {
            if (binding.etMessage.text.toString().trim().isNotEmpty()) {
                viewModel.sendMessageToUser(binding.etMessage.text.toString().trim())
                binding.etMessage.text.clear()
            } else {
                binding.etMessage.error = "Please enter message"
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
        }
        viewModel.message.observe(this) {
            binding.etMessage.text.toString()
        }
    }
}