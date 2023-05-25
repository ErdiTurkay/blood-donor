package com.erdi.blooddonor.utils

import android.content.Context
import com.erdi.blooddonor.R
import com.erdi.blooddonor.data.model.NotificationItem
import com.erdi.blooddonor.data.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext val context: Context,
) {

    companion object {
        private const val JWT_TOKEN = "jwt_token"
        private const val NOTIFICATION_TOKEN = "notification_token"
        private const val USER = "user"
        private const val NOTIFICATION = "notification"
    }

    fun saveAuthToken(token: String) {
        saveString(JWT_TOKEN, token)
    }

    fun getToken(): String? {
        return getString(JWT_TOKEN)
    }

    fun saveNotificationToken(notificationToken: String) {
        saveString(NOTIFICATION_TOKEN, notificationToken)
    }

    fun getNotificationToken(): String? {
        return getString(NOTIFICATION_TOKEN)
    }

    fun clearNotificationsAndRefresh(): MutableList<NotificationItem> {
        saveNotificationList(mutableListOf())
        return getNotificationList()
    }

    fun setNotification(title: String, message: String, date: String, postId: String) {
        val notificationList = getNotificationList()
        val notification = NotificationItem(title, message, date, postId)
        notificationList.add(notification)
        saveNotificationList(notificationList)
    }

    fun getNotificationList(): MutableList<NotificationItem> {
        val notificationJson = getString(NOTIFICATION)
        return if (notificationJson != null) {
            val notificationType = object : TypeToken<MutableList<NotificationItem>>() {}.type
            Gson().fromJson(notificationJson, notificationType)
        } else {
            mutableListOf()
        }
    }

    private fun saveNotificationList(notificationList: MutableList<NotificationItem>) {
        val notificationJson = Gson().toJson(notificationList)
        saveString(NOTIFICATION, notificationJson)
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
