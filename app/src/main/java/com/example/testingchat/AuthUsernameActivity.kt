package com.example.testingchat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.testingchat.databinding.ActivityAuthUsernameBinding

class AuthUsernameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthUsernameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthUsernameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLetMeIn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this@AuthUsernameActivity)
            finish()
        }
    }
}