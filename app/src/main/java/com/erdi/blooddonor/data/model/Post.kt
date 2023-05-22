package com.erdi.blooddonor.data.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("_id")
    val id: String,

    val location: Location,
    val user: PostUser,
    val patientName: String,
    val patientSurname: String,
    val patientAge: Int,
    val patientBloodType: String,
    val message: String,
    var replies: ArrayList<Reply>,
    val createdAt: String,
    val updatedAt: String,
)
