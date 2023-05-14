package com.example.blooddonor.data.model

import com.google.gson.annotations.SerializedName

data class Reply(
    @SerializedName("_id")
    var _id: String,

    @SerializedName("date")
    var date: String,

    @SerializedName("content")
    var content: String,

    @SerializedName("from")
    var from: ReplyUser,
)
