package com.erdi.blooddonor.feature.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erdi.blooddonor.data.api.request.SendNotificationTokenRequest
import com.erdi.blooddonor.data.api.response.AllPostsResponse
import com.erdi.blooddonor.data.api.response.BaseResponse
import com.erdi.blooddonor.data.api.response.GetMyPostsResponse
import com.erdi.blooddonor.data.api.response.SendNotificationTokenResponse
import com.erdi.blooddonor.data.model.Location
import com.erdi.blooddonor.data.repository.PostRepository
import com.erdi.blooddonor.data.repository.UserRepository
import com.erdi.blooddonor.utils.SessionManager
import com.erdi.blooddonor.utils.convertToErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    var postRepository: PostRepository,
    var userRepository: UserRepository,
    var sessionManager: SessionManager,
) : ViewModel() {
    val postResponse: MutableLiveData<BaseResponse<AllPostsResponse>> = MutableLiveData()
    val postInMyCityResponse: MutableLiveData<BaseResponse<GetMyPostsResponse>> = MutableLiveData()
    val postInMyDistrictResponse: MutableLiveData<BaseResponse<GetMyPostsResponse>> = MutableLiveData()
    private val notificationResponse: MutableLiveData<BaseResponse<SendNotificationTokenResponse>> = MutableLiveData()

    val location: MutableLiveData<Location> = MutableLiveData()

    init {
        getAllPosts()
        getPostsInMyCity()
        getPostsInMyDistrict()
    }

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

    fun getPostsInMyCity() {
        updateLocation()

        postInMyCityResponse.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = postRepository.getPostWithCity(location.value!!.city)

                if (response.code() == HttpURLConnection.HTTP_OK) {
                    postInMyCityResponse.value = BaseResponse.Success(response.body())
                } else {
                    val json = response.errorBody()?.string()
                    val errorObject = json.convertToErrorResponse()
                    postInMyCityResponse.value = BaseResponse.Error(errorObject.message)
                }
            } catch (ex: Exception) {
                postInMyCityResponse.value = BaseResponse.Error(ex.message)
            }
        }
    }

    fun getPostsInMyDistrict() {
        updateLocation()

        postInMyDistrictResponse.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = postRepository.getPostWithDistrict(location.value!!.city, location.value!!.district)

                if (response.code() == HttpURLConnection.HTTP_OK) {
                    postInMyDistrictResponse.value = BaseResponse.Success(response.body())
                } else {
                    val json = response.errorBody()?.string()
                    val errorObject = json.convertToErrorResponse()
                    postInMyDistrictResponse.value = BaseResponse.Error(errorObject.message)
                }
            } catch (ex: Exception) {
                postInMyDistrictResponse.value = BaseResponse.Error(ex.message)
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

    private fun updateLocation() {
        location.value = sessionManager.getUser().location
    }
}
