package com.example.blooddonor.data.api.response

import com.example.blooddonor.data.model.User

data class ChangePhoneNumberResponse(
    val message: String,
    val user: User,
)
