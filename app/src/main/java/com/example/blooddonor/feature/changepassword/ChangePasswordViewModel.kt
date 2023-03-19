package com.example.blooddonor.feature.changepassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooddonor.data.api.request.ChangePasswordRequest
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.data.api.response.ChangePasswordResponse
import com.example.blooddonor.data.repository.UserRepository
import com.example.blooddonor.utils.convertToErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    var userRepository: UserRepository,
) : ViewModel() {
    val responseResult: MutableLiveData<BaseResponse<ChangePasswordResponse>> = MutableLiveData()

    fun changePassword(oldPassword: String, newPassword: String) {
        responseResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val changePasswordRequest = ChangePasswordRequest(oldPassword, newPassword)
                val response = userRepository.changePassword(changePasswordRequest)

                if (response.isSuccessful) {
                    responseResult.value = BaseResponse.Success(response.body())
                } else {
                    val json = response.errorBody()?.string()
                    val errorObject = json.convertToErrorResponse()
                    responseResult.value = BaseResponse.Error(errorObject.message)
                }
            } catch (ex: Exception) {
                responseResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}