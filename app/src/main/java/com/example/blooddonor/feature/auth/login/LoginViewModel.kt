package com.example.blooddonor.feature.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooddonor.data.api.request.LoginRequest
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.data.api.response.ChangePasswordResponse
import com.example.blooddonor.data.api.response.LoginResponse
import com.example.blooddonor.repository.UserRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    var userRepository: UserRepository
): ViewModel() {
    val loginResult: MutableLiveData<BaseResponse<LoginResponse>> = MutableLiveData()

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
                    val errorObject = Gson().fromJson(json, LoginResponse::class.java)
                    loginResult.value = BaseResponse.Error(errorObject.message)
                }
            } catch (ex: Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}