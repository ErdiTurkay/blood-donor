package com.erdi.blooddonor.data.api.response

import com.erdi.blooddonor.data.model.Post

data class CreateNewPostResponse(
    var message: String,
    var post: Post,
)
