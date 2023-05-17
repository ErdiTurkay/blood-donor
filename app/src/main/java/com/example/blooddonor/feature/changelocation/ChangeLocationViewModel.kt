package com.example.blooddonor.feature.changelocation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooddonor.data.api.request.ChangeLocationRequest
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.data.api.response.ChangeLocationResponse
import com.example.blooddonor.data.model.Location
import com.example.blooddonor.data.repository.UserRepository
import com.example.blooddonor.utils.convertToErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeLocationViewModel @Inject constructor(
    var userRepository: UserRepository,
) : ViewModel() {
    val responseResult: MutableLiveData<BaseResponse<ChangeLocationResponse>> = MutableLiveData()

    fun changeLocation(newLocation: Location) {
        responseResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val changeLocationRequest = ChangeLocationRequest(newLocation)
                val response = userRepository.changeLocation(changeLocationRequest)

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
