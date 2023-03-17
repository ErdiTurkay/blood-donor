package com.example.blooddonor.utils

import android.view.View
import com.example.blooddonor.data.api.response.ErrorResponse
import com.google.gson.Gson

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
