package com.example.blooddonor.data.model

import com.google.gson.annotations.SerializedName

data class ReplyUser(
    @SerializedName("_id")
    var _id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("surname")
    var surname: String,

    @SerializedName("dateOfBirth")
    var dateOfBirth: String,

    @SerializedName("bloodType")
    var bloodType: String,
)

fun ReplyUser.fullName(): String {
    return "$name $surname"
}