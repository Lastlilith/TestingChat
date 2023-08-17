package com.example.testingchat.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.testingchat.model.UserModel


object AndroidUtil {

    fun passUserModelAsIntent(intent: Intent, model: UserModel) {
        intent.putExtra("username", model.username)
        intent.putExtra("phone", model.phone)
        intent.putExtra("userId", model.id)
//        intent.putExtra("fcmToken", model.getFcmToken())
    }

    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun setProfileImage(context: Context, uri: Uri, image: ImageView) {
        Glide.with(context).load(uri).apply(RequestOptions.circleCropTransform()).into(image)
    }
}