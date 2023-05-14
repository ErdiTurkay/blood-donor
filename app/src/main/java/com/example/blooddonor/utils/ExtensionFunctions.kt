package com.example.blooddonor.utils

import android.view.View
import com.example.blooddonor.data.api.response.ErrorResponse
import com.example.blooddonor.data.model.Post
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

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

fun String?.convertToLocalDateTime(): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern(APIConstants.DATE_TIME_PATTERN)
    return LocalDateTime.parse(this, formatter)
}

fun String.convertToDate(): Date {
    val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    return format.parse(this) ?: Date()
}

fun Post.convertToJson(): String {
    return Gson().toJson(this)
}

fun String?.convertToPost(): Post {
    return Gson().fromJson(this, Post::class.java)
}

fun LocalDateTime?.convertToReadableDate(): String? {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return this?.format(formatter)
}
