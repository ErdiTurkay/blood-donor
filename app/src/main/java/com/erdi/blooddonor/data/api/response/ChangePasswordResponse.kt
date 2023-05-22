package com.erdi.blooddonor.data.api.response

import com.erdi.blooddonor.data.model.User

data class ChangePasswordResponse(
    var message: String,
    var user: User,
)
