package com.example.blooddonor.data.api.response

import com.example.blooddonor.data.model.Post
import com.google.gson.annotations.SerializedName

data class CreateNewPostResponse(
    @SerializedName("message")
    var message: String,

    @SerializedName("post")
    var post: Post,
)
