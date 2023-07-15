package com.example.testingchat.data.auth.initial

import com.google.gson.annotations.SerializedName

data class InitialAuthReponse(
    @SerializedName("is_success") val isSuccess: Boolean
)
