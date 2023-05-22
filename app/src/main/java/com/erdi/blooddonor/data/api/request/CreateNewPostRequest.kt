package com.erdi.blooddonor.data.api.request

import com.erdi.blooddonor.data.model.Location

data class CreateNewPostRequest(
    var patientName: String,
    var patientSurname: String,
    var patientAge: Int,
    var patientBloodType: String,
    var location: Location,
    var message: String,
)
