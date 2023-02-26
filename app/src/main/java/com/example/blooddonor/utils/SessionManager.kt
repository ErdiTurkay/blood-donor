package com.example.blooddonor.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.blooddonor.R

object SessionManager {

    private const val JWT_TOKEN = "jwt_token"
    const val NAME = "name"
    const val SURNAME = "surname"
    const val MAIL = "mail"

    fun saveAuthToken(context: Context, token: String) {
        saveString(context, JWT_TOKEN, token)
    }

    fun getToken(context: Context): String? {
        return getString(context, JWT_TOKEN)
    }

    fun saveString(context: Context, key: String, value: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()

    }

    fun getString(context: Context, key: String): String? {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return prefs.getString(key, null)
    }

    fun clearData(context: Context){
        val editor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }
}