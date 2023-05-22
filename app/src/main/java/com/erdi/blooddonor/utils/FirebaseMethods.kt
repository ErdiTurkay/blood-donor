package com.erdi.blooddonor.utils

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

fun sendReplyNotification(token: String, name: String, comment: String, postId: String) {
    val url = "https://fcm.googleapis.com/fcm/send"

    val bodyJson = JSONObject()
    bodyJson.put("to", token)
    bodyJson.put(
        "notification",
        JSONObject().also {
            it.put("title", "İlanınıza 1 yeni yorum geldi!")
            it.put("body", "$name: \"$comment\"")
        },
    )
    bodyJson.put(
        "data",
        JSONObject().apply {
            put("postId", postId)
        },
    )

    val request = Request.Builder()
        .url(url)
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "key=AAAA9LV2QmY:APA91bHLSkxb58w5CKU08zlr-xCib2RopN5pQAo0nPgNXZ7Q_cgFuOGsyeU9YlJ3sNtXsXx7wtqchZjsNBQaOKlCFQoU9Frv-ZrpBcjjAItW9RL08UVS8q7AI8MiqXdr7xnsG6evw7kh")
        .post(
            bodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType()),
        )
        .build()

    val client = OkHttpClient()

    client.newCall(request).enqueue(
        object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.d("Fayırbeys", "Başarıyla gönderildi.")
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("Fayırbeys", "Başarıyla gönderilEMEdi.")
            }
        },
    )
}
