package com.erdi.blooddonor.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    val id: String,

    var location: Location,
    val email: String,
    val name: String,
    val surname: String,
    val password: String,
    val bloodType: String,
    val dateOfBirth: String,
    val lastDonation: String,
    var phone: String,
    val chats: List<String>,
)
