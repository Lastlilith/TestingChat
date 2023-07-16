package com.example.testingchat.data.sign_up

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("refresh_token") val refresh_token: String,
    @SerializedName("access_token") val access_token: String,
    @SerializedName("user_id") val user_id: Int,
)
