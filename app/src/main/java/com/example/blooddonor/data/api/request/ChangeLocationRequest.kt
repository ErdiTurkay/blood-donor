package com.example.blooddonor.data.api.request

import com.example.blooddonor.data.model.Location
import com.google.gson.annotations.SerializedName

data class ChangeLocationRequest(
    @SerializedName("newLocation")
    var newLocation: Location,
)
