package com.erdi.blooddonor.data.repository

import com.erdi.blooddonor.data.api.ApiService
import com.erdi.blooddonor.data.api.request.CreateNewPostRequest
import com.erdi.blooddonor.data.api.request.ReplyPostRequest
import com.erdi.blooddonor.data.api.response.AllPostsResponse
import com.erdi.blooddonor.data.api.response.CreateNewPostResponse
import com.erdi.blooddonor.data.api.response.GetOnePostResponse
import com.erdi.blooddonor.data.api.response.ReplyPostResponse
import retrofit2.Response
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val api: ApiService,
) {
    suspend fun getAllPosts(): Response<AllPostsResponse> {
        return api.getAllPosts()
    }

    suspend fun getOnePost(postId: String): Response<GetOnePostResponse> {
        return api.getOnePost(postId)
    }

    suspend fun createNewPost(createNewPostRequest: CreateNewPostRequest): Response<CreateNewPostResponse> {
        return api.createNewPost(createNewPostRequest)
    }

    suspend fun replyPost(replyPostRequest: ReplyPostRequest): Response<ReplyPostResponse> {
        return api.replyPost(replyPostRequest)
    }
}
