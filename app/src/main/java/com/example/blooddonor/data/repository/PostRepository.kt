package com.example.blooddonor.data.repository

import com.example.blooddonor.data.api.ApiService
import com.example.blooddonor.data.api.response.AllPostsResponse
import retrofit2.Response
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val api: ApiService,
) {
    suspend fun getAllPosts(): Response<AllPostsResponse> {
        return api.getAllPosts()
    }
}
