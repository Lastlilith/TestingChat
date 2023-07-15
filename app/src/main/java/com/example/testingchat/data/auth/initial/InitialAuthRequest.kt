package com.example.testingchat.data.auth.initial

import com.google.gson.annotations.SerializedName

data class InitialAuthRequest(
    @SerializedName("phone") val phone: String
)
