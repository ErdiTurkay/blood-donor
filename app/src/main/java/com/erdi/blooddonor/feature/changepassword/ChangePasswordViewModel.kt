package com.erdi.blooddonor.feature.changepassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erdi.blooddonor.data.api.request.ChangePasswordRequest
import com.erdi.blooddonor.data.api.response.BaseResponse
import com.erdi.blooddonor.data.api.response.ChangePasswordResponse
import com.erdi.blooddonor.data.repository.UserRepository
import com.erdi.blooddonor.utils.convertToErrorResponse
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
