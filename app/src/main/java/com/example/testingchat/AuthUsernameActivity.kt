package com.example.testingchat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.testingchat.databinding.ActivityAuthUsernameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthUsernameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthUsernameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthUsernameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.e("POPO", "onCreate: in AuthUsernameActivity", )

        binding.btnLetMeIn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this@AuthUsernameActivity)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("POPO", "onDestroy: in AuthUsernameActivity", )
    }
}