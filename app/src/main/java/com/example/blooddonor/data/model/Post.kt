package com.example.blooddonor.data.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("location")
    val location: Location,

    @SerializedName("_id")
    val id: String,

    @SerializedName("user")
    val user: PostUser,

    @SerializedName("patientName")
    val patientName: String,

    @SerializedName("patientSurname")
    val patientSurname: String,

    @SerializedName("patientAge")
    val patientAge: Int,

    @SerializedName("patientBloodType")
    val patientBloodType: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("replies")
    var replies: ArrayList<Reply>,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String,
)
