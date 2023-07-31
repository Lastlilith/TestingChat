package com.example.testingchat

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.testingchat.databinding.ActivityChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    private lateinit var userId: String
    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        userId = intent.getStringExtra("userId") ?: ""

        observeViewModel()

        viewModel.loadUserNameFromFirestore(userId)

        binding.btnBack.setOnClickListener { finish() }
    }


    private fun observeViewModel() {
        viewModel.apply {
            username.observe(this@ChatActivity) { username ->
                binding.toolbarTUsername.text = username
            }
        }
    }
}