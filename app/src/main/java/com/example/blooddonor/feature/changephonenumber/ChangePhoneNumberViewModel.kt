package com.example.blooddonor.feature.changephonenumber

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooddonor.data.api.request.ChangePhoneNumberRequest
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.data.api.response.ChangePhoneNumberResponse
import com.example.blooddonor.data.repository.UserRepository
import com.example.blooddonor.utils.convertToErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePhoneNumberViewModel @Inject constructor(
    var userRepository: UserRepository,
) : ViewModel() {
    val responseResult: MutableLiveData<BaseResponse<ChangePhoneNumberResponse>> = MutableLiveData()

    fun changePhoneNumber(phoneNumber: String) {
        responseResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val changePhoneNumberRequest = ChangePhoneNumberRequest(phoneNumber)
                val response = userRepository.changePhoneNumber(changePhoneNumberRequest)

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
