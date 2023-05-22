package com.erdi.blooddonor.data.api.response

import com.erdi.blooddonor.data.model.User

data class SendNotificationTokenResponse(
    val message: String,
    val user: User,
)
