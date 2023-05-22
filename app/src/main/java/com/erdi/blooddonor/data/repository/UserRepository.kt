package com.erdi.blooddonor.data.repository

import com.erdi.blooddonor.data.api.ApiService
import com.erdi.blooddonor.data.api.request.ChangeLocationRequest
import com.erdi.blooddonor.data.api.request.ChangePasswordRequest
import com.erdi.blooddonor.data.api.request.ChangePhoneNumberRequest
import com.erdi.blooddonor.data.api.request.LoginRequest
import com.erdi.blooddonor.data.api.request.RegisterRequest
import com.erdi.blooddonor.data.api.request.SendNotificationTokenRequest
import com.erdi.blooddonor.data.api.response.AuthResponse
import com.erdi.blooddonor.data.api.response.ChangeLocationResponse
import com.erdi.blooddonor.data.api.response.ChangePasswordResponse
import com.erdi.blooddonor.data.api.response.ChangePhoneNumberResponse
import com.erdi.blooddonor.data.api.response.SendNotificationTokenResponse
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: ApiService,
) {
    suspend fun loginUser(loginRequest: LoginRequest): Response<AuthResponse> {
        return api.loginUser(loginRequest = loginRequest)
    }

    suspend fun registerUser(registerRequest: RegisterRequest): Response<AuthResponse> {
        return api.registerUser(registerRequest = registerRequest)
    }

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Response<ChangePasswordResponse> {
        return api.changePassword(changePasswordRequest = changePasswordRequest)
    }

    suspend fun changePhoneNumber(changePhoneNumberRequest: ChangePhoneNumberRequest): Response<ChangePhoneNumberResponse> {
        return api.changePhoneNumber(changePhoneNumberRequest = changePhoneNumberRequest)
    }

    suspend fun changeLocation(changeLocationRequest: ChangeLocationRequest): Response<ChangeLocationResponse> {
        return api.changeLocation(changeLocationRequest = changeLocationRequest)
    }

    suspend fun sendNotificationToken(sendNotificationTokenRequest: SendNotificationTokenRequest): Response<SendNotificationTokenResponse>{
        return api.sendNotificationToken(sendNotificationTokenRequest = sendNotificationTokenRequest)
    }
}
