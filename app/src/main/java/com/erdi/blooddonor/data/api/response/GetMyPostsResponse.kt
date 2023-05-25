package com.erdi.blooddonor.data.api.response

import com.erdi.blooddonor.data.model.Post

data class GetMyPostsResponse(
    var posts: List<Post>? = null,
)
