package com.example.blooddonor.repository

import com.example.blooddonor.data.api.ApiService
import com.example.blooddonor.data.api.request.LoginRequest
import com.example.blooddonor.data.api.response.LoginResponse
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse>? {
        return api.loginUser(loginRequest = loginRequest)
    }
}