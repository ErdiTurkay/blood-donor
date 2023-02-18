package com.example.blooddonor.data.api

import com.example.blooddonor.data.api.request.LoginRequest
import com.example.blooddonor.data.api.response.LoginResponse
import com.example.blooddonor.utils.Constant
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST(Constant.LOGIN_URL)
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>
}