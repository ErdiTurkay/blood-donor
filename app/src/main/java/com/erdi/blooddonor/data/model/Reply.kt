package com.erdi.blooddonor.data.model

import com.google.gson.annotations.SerializedName

data class Reply(
    @SerializedName("_id")
    var id: String,

    var date: String,
    var content: String,
    var from: ReplyUser,
)
