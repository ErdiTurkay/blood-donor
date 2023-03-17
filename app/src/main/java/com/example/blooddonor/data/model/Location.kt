package com.example.blooddonor.data.model

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("city")
    val city: String,

    @SerializedName("district")
    val district: String
)
