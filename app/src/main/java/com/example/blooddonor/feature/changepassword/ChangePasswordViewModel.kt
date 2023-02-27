package com.example.blooddonor.feature.changepassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooddonor.data.api.request.ChangePasswordRequest
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.data.api.response.ChangePasswordResponse
import com.example.blooddonor.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    var userRepository: UserRepository
): ViewModel() {
    val responseResult: MutableLiveData<BaseResponse<ChangePasswordResponse>> = MutableLiveData()

    fun changePassword(password: String) {
        responseResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val changePasswordRequest = ChangePasswordRequest(password)
                val response = userRepository.changePassword(changePasswordRequest)

                if (response.code() == HttpURLConnection.HTTP_OK) {
                    responseResult.value = BaseResponse.Success(response.body())
                } else {
                    response.body()?.message
                    responseResult.value = BaseResponse.Error(response.message())
                }
            } catch (ex: Exception) {
                responseResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}