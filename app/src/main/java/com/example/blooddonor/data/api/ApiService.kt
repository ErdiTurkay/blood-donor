package com.example.blooddonor.data.api

import com.example.blooddonor.data.api.request.ChangePasswordRequest
import com.example.blooddonor.data.api.request.ChangePhoneNumberRequest
import com.example.blooddonor.data.api.request.LoginRequest
import com.example.blooddonor.data.api.request.RegisterRequest
import com.example.blooddonor.data.api.response.*
import com.example.blooddonor.utils.APIConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST(APIConstants.LOGIN_URL)
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST(APIConstants.REGISTER_URL)
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST(APIConstants.UPDATE_PASSWORD_URL)
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<ChangePasswordResponse>

    @POST(APIConstants.UPDATE_PHONE_NUMBER_URL)
    suspend fun changePhoneNumber(@Body changePhoneNumberRequest: ChangePhoneNumberRequest): Response<ChangePhoneNumberResponse>

    @GET(APIConstants.GET_ALL_POSTS)
    suspend fun getAllPosts(): Response<AllPostsResponse>
}
