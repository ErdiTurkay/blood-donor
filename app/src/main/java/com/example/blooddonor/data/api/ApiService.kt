package com.example.blooddonor.data.api

import com.example.blooddonor.data.api.request.ChangePasswordRequest
import com.example.blooddonor.data.api.request.LoginRequest
import com.example.blooddonor.data.api.request.RegisterRequest
import com.example.blooddonor.data.api.response.ChangePasswordResponse
import com.example.blooddonor.data.api.response.LoginResponse
import com.example.blooddonor.data.api.response.RegisterResponse
import com.example.blooddonor.utils.Constant
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST(Constant.LOGIN_URL)
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST(Constant.REGISTER_URL)
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST(Constant.UPDATE_PASSWORD_URL)
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<ChangePasswordResponse>
}