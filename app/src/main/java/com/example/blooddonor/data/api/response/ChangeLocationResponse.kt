package com.example.blooddonor.data.api.response

import com.example.blooddonor.data.model.User
import com.google.gson.annotations.SerializedName

data class ChangeLocationResponse(
    @SerializedName("message")
    var message: String,

    @SerializedName("user")
    var user: User,
)
