package com.erdi.blooddonor.utils

object APIConstants {
    //const val BASE_URL = "http://10.0.2.2:8080/"
    const val BASE_URL = "http://blooddonor.com.tr/"
    const val LOGIN_URL = "auth/login"
    const val REGISTER_URL = "auth/signup"
    const val UPDATE_PASSWORD_URL = "user/password"
    const val UPDATE_PHONE_NUMBER_URL = "user/phone"
    const val UPDATE_LOCATION = "user/location"
    const val GET_ALL_POSTS = "post"
    const val REPLY_POST = "post/reply"
    const val CREATE_NEW_POST = "post"
    const val SEND_NOTIFICATION_TOKEN = "user/notificationToken"
    const val GET_MY_POSTS = "post/myPosts"
    const val POST_WITH_ID = "post/{postId}"

    //
    const val DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val DATE_TIME_PATTERN_WITHOUT_HOUR = "yyyy-MM-dd"
}
