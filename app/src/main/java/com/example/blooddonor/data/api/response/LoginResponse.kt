package com.example.blooddonor.data.api.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message")
    var message: String,
    @SerializedName("token")
    var token: String
)