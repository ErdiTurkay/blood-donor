package com.erdi.blooddonor.data.api.request

import com.erdi.blooddonor.data.model.Location
import java.util.Date

data class RegisterRequest(
    var email: String,
    var password: String,
    var name: String,
    var surname: String,
    var bloodType: String,
    var location: Location,
    var dateOfBirth: Date,
    var lastDonation: Date,
    var phone: String,
)
