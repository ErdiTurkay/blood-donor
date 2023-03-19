package com.example.blooddonor.data.api.response

import com.example.blooddonor.data.model.Post
import com.google.gson.annotations.SerializedName

data class AllPostsResponse(
    @SerializedName("posts")
    var posts: List<Post>? = null,

    @SerializedName("totalItems")
    var totalItems: Int,
)
