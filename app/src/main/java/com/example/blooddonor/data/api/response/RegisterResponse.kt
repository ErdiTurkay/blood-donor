package com.example.blooddonor.data.api.response

import com.example.blooddonor.data.model.User
import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("message")
    var message: String,

    @SerializedName("token")
    var token: String,

    @SerializedName("user")
    var user: User
)