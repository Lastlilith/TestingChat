package com.example.testingchat.app.activities.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.testingchat.R
import com.example.testingchat.app.activities.auth.AuthPhoneActivity
import java.util.Timer
import java.util.TimerTask

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                val intent = Intent(this@SplashActivity, AuthPhoneActivity::class.java)
                startActivity(intent)
                Animatoo.animateFade(this@SplashActivity)
                finish()
            }
        }, DELAY)

    }

    companion object {
        private const val DELAY: Long = 1000
    }
}