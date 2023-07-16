package com.example.testingchat.data.sign_up

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("phone") val phone: String,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
)
