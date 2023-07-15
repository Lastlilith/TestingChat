package com.example.testingchat.data.auth

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("phone") val phone: String
)
