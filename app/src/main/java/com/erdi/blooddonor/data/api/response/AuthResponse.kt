package com.erdi.blooddonor.data.api.response

import com.erdi.blooddonor.data.model.User

data class AuthResponse(
    var message: String,
    var token: String,
    var user: User,
)
