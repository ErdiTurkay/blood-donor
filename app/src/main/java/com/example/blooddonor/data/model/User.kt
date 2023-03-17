package com.example.blooddonor.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("location")
    val location: Location,

    @SerializedName("_id")
    val id: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("surname")
    val surname: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("bloodType")
    val bloodType: String,

    @SerializedName("dateOfBirth")
    val dateOfBirth: String,

    @SerializedName("lastDonation")
    val lastDonation: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("chats")
    val chats: List<String>,
)
