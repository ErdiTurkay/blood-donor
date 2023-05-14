package com.example.blooddonor.data.api.request

import com.example.blooddonor.data.model.Location
import com.google.gson.annotations.SerializedName
import java.util.Date

data class RegisterRequest(
    @SerializedName("email")
    var email: String,

    @SerializedName("password")
    var password: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("surname")
    var surname: String,

    @SerializedName("bloodType")
    var bloodType: String,

    @SerializedName("location")
    var location: Location,

    @SerializedName("dateOfBirth")
    var dateOfBirth: Date,

    @SerializedName("lastDonation")
    var lastDonation: Date,

    @SerializedName("phone")
    var phone: String,
)
