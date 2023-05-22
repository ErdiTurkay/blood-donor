package com.erdi.blooddonor.data.model

data class Location(
    val city: String,
    val district: String,
)

fun Location.fullLocation(): String {
    return "$district/$city"
}
