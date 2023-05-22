package com.erdi.blooddonor.data.api.request

data class ChangePasswordRequest(
    var oldPassword: String,
    var newPassword: String,
)
