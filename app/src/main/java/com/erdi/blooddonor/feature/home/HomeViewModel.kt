package com.erdi.blooddonor.feature.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erdi.blooddonor.data.api.response.AllPostsResponse
import com.erdi.blooddonor.data.api.response.BaseResponse
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

    init {
        getAllPosts()
    }

    private fun getAllPosts() {
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
