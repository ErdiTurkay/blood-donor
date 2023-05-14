package com.example.blooddonor.data.model

import com.example.blooddonor.utils.convertToLocalDateTime
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class PostUser(
    @SerializedName("_id")
    var _id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("surname")
    var surname: String,

    @SerializedName("dateOfBirth")
    var dateOfBirth: String,

    @SerializedName("phone")
    var phone: String,
)

fun PostUser.fullName(): String {
    return "$name $surname"
}

fun PostUser.age(): Int {
    return ChronoUnit.YEARS.between(dateOfBirth.convertToLocalDateTime(), LocalDateTime.now()).toInt()
}
