package com.erdi.blooddonor.data.api.request

import com.erdi.blooddonor.data.model.Location

data class ChangeLocationRequest(
    var newLocation: Location,
)
