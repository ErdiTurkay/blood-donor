package com.example.blooddonor.feature.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooddonor.data.api.request.LoginRequest
import com.example.blooddonor.data.api.response.AllPostsResponse
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.data.api.response.LoginResponse
import com.example.blooddonor.data.repository.PostRepository
import com.example.blooddonor.utils.convertToErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    var postRepository: PostRepository,
) : ViewModel() {
    val postResponse: MutableLiveData<BaseResponse<AllPostsResponse>> = MutableLiveData()

    fun getAllPosts() {
        postResponse.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = postRepository.getAllPosts()

                if (response.code() == HttpURLConnection.HTTP_OK) {
                    postResponse.value = BaseResponse.Success(response.body())
                } else {
                    val json = response.errorBody()?.string()
                    val errorObject = json.convertToErrorResponse()
                    postResponse.value = BaseResponse.Error(errorObject.message)
                }
            } catch (ex: Exception) {
                postResponse.value = BaseResponse.Error(ex.message)
            }
        }
    }
}