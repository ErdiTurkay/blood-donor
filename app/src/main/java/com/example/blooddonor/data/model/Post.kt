package com.example.blooddonor.data.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("location")
    val location: Location,

    @SerializedName("_id")
    val _id: String,

    @SerializedName("user")
    val user: PostUser,

    @SerializedName("bloodType")
    val bloodType: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String,
)
