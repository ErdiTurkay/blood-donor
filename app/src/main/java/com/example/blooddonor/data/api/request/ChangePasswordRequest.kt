package com.example.blooddonor.data.api.request

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("password")
    var password: String
)