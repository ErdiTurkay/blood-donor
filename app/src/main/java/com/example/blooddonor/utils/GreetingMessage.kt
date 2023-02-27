package com.example.blooddonor.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import com.example.blooddonor.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GreetingMessage @Inject constructor(
    @ApplicationContext val context: Context
) {
    companion object {
        private const val NIGHT_LIMIT_1 = 4
        private const val NIGHT_LIMIT_2 = 22
        private const val MORNING_LIMIT = 12
        private const val DAY_LIMIT = 18
    }

    fun getTimeString() : String {
        val cal = Calendar.getInstance()
        val hours = cal.get(Calendar.HOUR_OF_DAY)

        return context.run {
            if (hours < NIGHT_LIMIT_1 || hours > NIGHT_LIMIT_2) {
                getString(R.string.good_night)
            } else if (hours < MORNING_LIMIT) {
                getString(R.string.good_morning)
            } else if (hours < DAY_LIMIT) {
                getString(R.string.good_day)
            } else {
                getString(R.string.good_evening)
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getTimeDrawable() : Drawable? {
        val cal = Calendar.getInstance()
        val hours = cal.get(Calendar.HOUR_OF_DAY)

        return context.run {
            if (hours < NIGHT_LIMIT_1 || hours > NIGHT_LIMIT_2) {
                getDrawable(R.drawable.night)
            } else if (hours < MORNING_LIMIT) {
                getDrawable(R.drawable.morning)
            } else if (hours < DAY_LIMIT) {
                getDrawable(R.drawable.day)
            } else {
                getDrawable(R.drawable.evening)
            }
        }
    }
}