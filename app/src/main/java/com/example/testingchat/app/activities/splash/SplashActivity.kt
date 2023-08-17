package com.example.testingchat.app.activities.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        // need to add inside if statement check for isLogged in check && rest
        if (intent.extras !=null) {
            //from notification
            val userId = intent.extras!!.getString("userId")
            FirebaseUtil.allUserCollectionReference().document(userId!!).get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = it.result.toObject(UserModel::class.java)!!
                        val mainIntent = Intent(this, MainActivity::class.java)
                        mainIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                        startActivity(mainIntent)
                        val intent = Intent(this, ChatActivity::class.java)
                        intent.putExtra("userId", user.id.toString())
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        this.startActivity(intent)
                        finish()
                    }
                }
        } else {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    val intent = Intent(this@SplashActivity, AuthPhoneActivity::class.java)
                    startActivity(intent)
                    Animatoo.animateFade(this@SplashActivity)
                    finish()
                }
            }, DELAY)
        }
    }

    companion object {
        private const val DELAY: Long = 1000
    }
}