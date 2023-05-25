package com.erdi.blooddonor.feature.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erdi.blooddonor.data.api.request.LoginRequest
import com.erdi.blooddonor.data.api.request.SendNotificationTokenRequest
import com.erdi.blooddonor.data.api.response.AuthResponse
import com.erdi.blooddonor.data.api.response.BaseResponse
import com.erdi.blooddonor.data.api.response.SendNotificationTokenResponse
import com.erdi.blooddonor.data.repository.UserRepository
import com.erdi.blooddonor.utils.convertToErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    var userRepository: UserRepository,
) : ViewModel() {
    val loginResult: MutableLiveData<BaseResponse<AuthResponse>> = MutableLiveData()
    private val notificationResponse: MutableLiveData<BaseResponse<SendNotificationTokenResponse>> = MutableLiveData()

    fun loginUser(email: String, password: String) {
        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(email, password)
                val response = userRepository.loginUser(loginRequest)

                if (response.code() == HttpURLConnection.HTTP_OK) {
                    loginResult.value = BaseResponse.Success(response.body())
                } else {
                    val json = response.errorBody()?.string()
                    val errorObject = json.convertToErrorResponse()
                    loginResult.value = BaseResponse.Error(errorObject.message)
                }
            } catch (ex: Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
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
