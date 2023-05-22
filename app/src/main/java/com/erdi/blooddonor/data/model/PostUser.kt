package com.erdi.blooddonor.data.model

import com.erdi.blooddonor.utils.convertToLocalDateTime
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class PostUser(
    @SerializedName("_id")
    var id: String,

    var name: String,
    var surname: String,
    var dateOfBirth: String,
    var phone: String,
    var notificationToken: String?,
)

fun PostUser.fullName(): String {
    return "$name $surname"
}

fun PostUser.age(): Int {
    return ChronoUnit.YEARS.between(dateOfBirth.convertToLocalDateTime(), LocalDateTime.now()).toInt()
}
