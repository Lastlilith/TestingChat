package com.example.testingchat.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("phone") val phone: String = "",
    @SerializedName("name") val name: String? = null,
    @SerializedName("username") val username: String = "",
    @SerializedName("birthday") val birthday: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("user_id") val id: Int = 0,
//    @SerializedName("avatar") val image: String = ""
) {
    constructor() : this("", null, "", null, null, 0)
}
