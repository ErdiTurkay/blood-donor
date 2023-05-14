package com.example.blooddonor.feature.auth.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooddonor.data.api.request.RegisterRequest
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.data.api.response.RegisterResponse
import com.example.blooddonor.data.model.Location
import com.example.blooddonor.data.repository.UserRepository
import com.example.blooddonor.utils.convertToErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    var userRepository: UserRepository,
) : ViewModel() {
    val registerResult: MutableLiveData<BaseResponse<RegisterResponse>> = MutableLiveData()

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
}
