package com.example.blooddonor.data.api.request

import com.example.blooddonor.data.model.Location
import com.google.gson.annotations.SerializedName

data class CreateNewPostRequest(
    @SerializedName("patientName")
    var patientName: String,

    @SerializedName("patientSurname")
    var patientSurname: String,

    @SerializedName("patientAge")
    var patientAge: Int,

    @SerializedName("patientBloodType")
    var patientBloodType: String,

    @SerializedName("location")
    var location: Location,

    @SerializedName("message")
    var message: String,
)
