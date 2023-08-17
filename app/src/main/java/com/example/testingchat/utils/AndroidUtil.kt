package com.example.testingchat.utils

import android.content.Intent
import com.example.testingchat.model.UserModel


object AndroidUtil {

    fun passUserModelAsIntent(intent: Intent, model: UserModel) {
        intent.putExtra("username", model.username)
        intent.putExtra("phone", model.phone)
        intent.putExtra("userId", model.id)
//        intent.putExtra("fcmToken", model.getFcmToken())
    }
}