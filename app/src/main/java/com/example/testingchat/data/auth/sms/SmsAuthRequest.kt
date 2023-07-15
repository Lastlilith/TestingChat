package com.example.testingchat.data.auth.sms

import com.google.gson.annotations.SerializedName

data class SmsAuthRequest(
    @SerializedName("phone") val phone: String,
    @SerializedName("code") val code: String
)