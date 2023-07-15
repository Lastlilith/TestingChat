package com.example.testingchat.data.auth

import com.google.gson.annotations.SerializedName

data class AuthReponse(
    @SerializedName("is_success") val isSuccess: Boolean
)
