package com.example.testingchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testingchat.databinding.ActivityMainBinding

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

    }
}