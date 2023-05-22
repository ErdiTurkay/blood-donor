package com.erdi.blooddonor.feature.auth.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erdi.blooddonor.data.api.request.RegisterRequest
import com.erdi.blooddonor.data.api.request.SendNotificationTokenRequest
import com.erdi.blooddonor.data.api.response.AuthResponse
import com.erdi.blooddonor.data.api.response.BaseResponse
import com.erdi.blooddonor.data.api.response.SendNotificationTokenResponse
import com.erdi.blooddonor.data.model.Location
import com.erdi.blooddonor.data.repository.UserRepository
import com.erdi.blooddonor.utils.convertToErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    var userRepository: UserRepository,
) : ViewModel() {
    val registerResult: MutableLiveData<BaseResponse<AuthResponse>> = MutableLiveData()
    val notificationResponse: MutableLiveData<BaseResponse<SendNotificationTokenResponse>> = MutableLiveData()

    fun registerUser(
        name: String,
        surname: String,
        email: String,
        password: String,
        bloodType: String,
        location: Location,
        dateOfBirth: Date,
        phone: String,
        lastDonation: Date,
    ) {
        registerResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val registerRequest = RegisterRequest(email, password, name, surname, bloodType, location, dateOfBirth, lastDonation, phone)
                val response = userRepository.registerUser(registerRequest)

                if (response.code() == HttpURLConnection.HTTP_CREATED) {
                    registerResult.value = BaseResponse.Success(response.body())
                } else {
                    val json = response.errorBody()?.string()
                    val errorObject = json.convertToErrorResponse()
                    registerResult.value = BaseResponse.Error(errorObject.message)
                }
            } catch (ex: Exception) {
                registerResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

    fun sendNotificationToken(token: String) {
        notificationResponse.value = BaseResponse.Loading()

        viewModelScope.launch {
            try {
                val sendNotificationTokenRequest = SendNotificationTokenRequest(token)
                val response = userRepository.sendNotificationToken(sendNotificationTokenRequest)

                if (response.isSuccessful) {
                    Log.d("Fayırbeys", "Backende yolladım: $token")
                    notificationResponse.value = BaseResponse.Success(response.body())
                } else {
                    val json = response.errorBody()?.string()
                    val errorObject = json.convertToErrorResponse()
                    notificationResponse.value = BaseResponse.Error(errorObject.message)
                }
            } catch (ex: Exception) {
                notificationResponse.value = BaseResponse.Error(ex.message)
            }
        }
    }
}
