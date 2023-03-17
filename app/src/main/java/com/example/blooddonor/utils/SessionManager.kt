package com.example.blooddonor.utils

import android.content.Context
import com.example.blooddonor.R
import com.example.blooddonor.data.model.User
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext val context: Context
){

    companion object {
        private const val JWT_TOKEN = "jwt_token"
        const val USER = "user"
    }

    fun saveAuthToken(token: String) {
        saveString(JWT_TOKEN, token)
    }

    fun getToken(): String? {
        return getString(JWT_TOKEN)
    }

    fun getFullName(): String {
        val user = getUser()
        return user.name + " " + user.surname
    }

    fun saveUser(user: User) {
        val json = Gson().toJson(user)
        saveString(USER, json)
    }

    fun getUser(): User {
        val userJson = getString(USER)
        return Gson().fromJson(userJson, User::class.java)
    }

    fun saveString(key: String, value: String) {
        val prefs = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String? {
        val prefs = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return prefs.getString(key, null)
    }

    fun clearData() {
        val editor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }
}