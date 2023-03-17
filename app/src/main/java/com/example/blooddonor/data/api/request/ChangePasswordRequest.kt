package com.example.blooddonor.data.api.request

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("oldPassword")
    var oldPassword: String,

    @SerializedName("newPassword")
    var newPassword: String
)