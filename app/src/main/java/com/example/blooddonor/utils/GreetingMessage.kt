package com.example.blooddonor.utils

import android.content.Context
import com.example.blooddonor.R
import java.util.*

object GreetingMessage {

    private const val NIGHT_LIMIT_1 = 4
    private const val NIGHT_LIMIT_2 = 22
    private const val MORNING_LIMIT = 12
    private const val DAY_LIMIT = 18

    fun getTime(context: Context) : String {
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
}