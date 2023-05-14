package com.example.blooddonor.data.api.request

import com.google.gson.annotations.SerializedName

data class ReplyPostRequest(
    @SerializedName("postId")
    var postId: String,

    @SerializedName("comment")
    var comment: String,
)
