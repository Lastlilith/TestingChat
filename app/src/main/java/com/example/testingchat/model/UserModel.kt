package com.example.testingchat.model

import com.google.firebase.Timestamp

data class UserModel(
    val phone: String = "",
    val name: String? = null,
    val username: String = "",
    var birthday: String? = null,
    var city: String? = null,
    val id: String? = null,
    val createdTimeStamp: Timestamp? = null,
//    @SerializedName("avatar") val image: String = ""
) {
    constructor() : this("", null, "", null, null, null)
}
