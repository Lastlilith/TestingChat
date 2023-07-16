package com.example.testingchat.data.auth.initial

import com.google.gson.annotations.SerializedName

data class InitialAuthReponse(
    @SerializedName ("refresh_token") val refreshToken: String,
    @SerializedName ("access_token") val accessToken: String,
    @SerializedName ("user_id") val user_id: Int,
    @SerializedName("is_success") val isSuccess: Boolean
)
