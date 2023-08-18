package com.example.testingchat.app.activities.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.testingchat.R
import com.example.testingchat.app.activities.MainActivity
import com.example.testingchat.app.activities.auth.AuthPhoneActivity
import com.example.testingchat.app.activities.chat.ChatActivity
import com.example.testingchat.model.UserModel
import com.example.testingchat.utils.FirebaseUtil
import java.util.Timer
import java.util.TimerTask

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Log.e("POPO", "is user logged in: ${FirebaseUtil.isLoggedIn()} ", )

        if (intent.extras != null) {
            val userId = intent.extras!!.getString("userId")
            FirebaseUtil.allUserCollectionReference().document(userId!!).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result.toObject(UserModel::class.java)!!
                        startMainActivity(user)
                    }
                }
        } else if(FirebaseUtil.isLoggedIn()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        else {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    startAuthPhoneActivity()
                }
            }, DELAY)
        }
    }

    private fun startMainActivity(user: UserModel) {
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

        val chatIntent = Intent(this, ChatActivity::class.java)
        chatIntent.putExtra("userId", user.id.toString())

        startActivity(mainIntent)
        startActivity(chatIntent)
        Animatoo.animateFade(this)
        finish()
    }

    private fun startAuthPhoneActivity() {
        val intent = Intent(this, AuthPhoneActivity::class.java)
        startActivity(intent)
        Animatoo.animateFade(this)
        finish()
    }

    companion object {
        private const val DELAY: Long = 1000
    }
}

