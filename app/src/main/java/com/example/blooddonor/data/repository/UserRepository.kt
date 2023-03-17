package com.example.blooddonor.data.repository

import com.example.blooddonor.data.api.ApiService
import com.example.blooddonor.data.api.request.ChangePasswordRequest
import com.example.blooddonor.data.api.request.ChangePhoneNumberRequest
import com.example.blooddonor.data.api.request.LoginRequest
import com.example.blooddonor.data.api.request.RegisterRequest
import com.example.blooddonor.data.api.response.ChangePasswordResponse
import com.example.blooddonor.data.api.response.ChangePhoneNumberResponse
import com.example.blooddonor.data.api.response.LoginResponse
import com.example.blooddonor.data.api.response.RegisterResponse
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: ApiService,
) {
    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse> {
        return api.loginUser(loginRequest = loginRequest)
    }

    suspend fun registerUser(registerRequest: RegisterRequest): Response<RegisterResponse> {
        return api.registerUser(registerRequest = registerRequest)
    }

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Response<ChangePasswordResponse> {
        return api.changePassword(changePasswordRequest = changePasswordRequest)
    }

    suspend fun changePhoneNumber(changePhoneNumberRequest: ChangePhoneNumberRequest): Response<ChangePhoneNumberResponse> {
        return api.changePhoneNumber(changePhoneNumberRequest = changePhoneNumberRequest)
    }
}
