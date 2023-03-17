package com.example.blooddonor.data.api.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("message")
    var message: String,
)
