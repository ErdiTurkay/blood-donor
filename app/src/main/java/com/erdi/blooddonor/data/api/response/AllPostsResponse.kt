package com.erdi.blooddonor.data.api.response

import com.erdi.blooddonor.data.model.Post

data class AllPostsResponse(
    var posts: List<Post>? = null,
    var totalItems: Int,
)
