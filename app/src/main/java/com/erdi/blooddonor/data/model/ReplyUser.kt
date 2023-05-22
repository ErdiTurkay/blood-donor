package com.erdi.blooddonor.data.model

import com.google.gson.annotations.SerializedName

data class ReplyUser(
    @SerializedName("_id")
    var id: String,

    var name: String,
    var surname: String,
    var dateOfBirth: String,
    var bloodType: String,
)

fun ReplyUser.fullName(): String {
    return "$name $surname"
}
