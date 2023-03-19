package com.example.blooddonor.utils

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.example.blooddonor.data.api.response.ErrorResponse
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.showOrHide(boolean: Boolean) {
    visibility = if (boolean) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

fun String?.convertToErrorResponse(): ErrorResponse {
    return Gson().fromJson(this, ErrorResponse::class.java)
}

@RequiresApi(Build.VERSION_CODES.O)
fun String?.convertToLocalDateTime(): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern(APIConstants.DATE_TIME_PATTERN)
    return LocalDateTime.parse(this, formatter)
}
