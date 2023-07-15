package com.example.testingchat.data.auth.sms

import com.google.gson.annotations.SerializedName

data class SmsAuthResponse(
    @SerializedName ("refresh_token") val refreshToken: String,
    @SerializedName ("access_token") val access_token: String,
    @SerializedName ("user_id") val user_id: Int,
    @SerializedName ("is_user_exists") val is_user_exists: Boolean,


)
