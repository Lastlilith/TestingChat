package com.example.testingchat.model

import com.google.firebase.Timestamp

data class UserModel(
    val phone: String = "",
    val name: String? = null,
    val username: String = "",
    val birthday: String? = null,
    val city: String? = null,
    val id: Int = 0,
    val createdTimeStamp: Timestamp? = null,
//    @SerializedName("avatar") val image: String = ""
) {
    constructor() : this("", null, "", null, null, 0)
}
