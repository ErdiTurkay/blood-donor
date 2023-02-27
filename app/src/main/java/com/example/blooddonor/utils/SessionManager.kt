package com.example.blooddonor.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.blooddonor.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext val context: Context
){

    companion object {
        private const val JWT_TOKEN = "jwt_token"
        const val NAME = "name"
        const val SURNAME = "surname"
        const val MAIL = "mail"
    }

    fun saveAuthToken(token: String) {
        saveString(JWT_TOKEN, token)
    }

    fun getToken(): String? {
        return getString(JWT_TOKEN)
    }

    fun saveString(key: String, value: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()

    }

    fun getString(key: String): String? {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return prefs.getString(key, null)
    }

    fun clearData(){
        val editor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }
}