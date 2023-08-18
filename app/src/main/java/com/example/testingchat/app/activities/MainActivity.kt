package com.example.testingchat.app.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.testingchat.app.fragments.recent_chats.ChatFragment
import com.example.testingchat.app.fragments.profile.ProfileFragment
import com.example.testingchat.R
import com.example.testingchat.databinding.ActivityMainBinding
import com.example.testingchat.utils.FirebaseUtil
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var chatFragment: ChatFragment
    private lateinit var profileFragment: ProfileFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatFragment = ChatFragment()
        profileFragment = ProfileFragment()

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_chat -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout, chatFragment).commit()
                    true
                }
                R.id.menu_profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout, profileFragment).commit()
                    true
                }
                else -> false
            }
        }
        binding.bottomNav.selectedItemId = R.id.menu_chat
        getFcmToken()
    }

    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.e("POPO", "token: $token", )
                FirebaseUtil.currentUserDetails().update("fcmToken", token)
            }
        }
    }
}