package com.example.blooddonor.data.repository

import com.example.blooddonor.data.api.ApiService
import com.example.blooddonor.data.api.request.CreateNewPostRequest
import com.example.blooddonor.data.api.request.ReplyPostRequest
import com.example.blooddonor.data.api.response.AllPostsResponse
import com.example.blooddonor.data.api.response.CreateNewPostResponse
import com.example.blooddonor.data.api.response.ReplyPostResponse
import retrofit2.Response
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val api: ApiService,
) {
    suspend fun getAllPosts(): Response<AllPostsResponse> {
        return api.getAllPosts()
    }

    suspend fun createNewPost(createNewPostRequest: CreateNewPostRequest): Response<CreateNewPostResponse> {
        return api.createNewPost(createNewPostRequest)
    }

    suspend fun replyPost(replyPostRequest: ReplyPostRequest): Response<ReplyPostResponse> {
        return api.replyPost(replyPostRequest)
    }
}
