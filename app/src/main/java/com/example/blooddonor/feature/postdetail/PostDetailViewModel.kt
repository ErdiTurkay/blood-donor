package com.example.blooddonor.feature.postdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooddonor.data.api.request.ReplyPostRequest
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.data.api.response.ReplyPostResponse
import com.example.blooddonor.data.repository.PostRepository
import com.example.blooddonor.utils.convertToErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    var postRepository: PostRepository,
) : ViewModel() {
    val responseResult: MutableLiveData<BaseResponse<ReplyPostResponse>> = MutableLiveData()

    fun replyPost(postId: String, comment: String) {
        responseResult.value = BaseResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val replyPostRequest = ReplyPostRequest(postId, comment)
                val response = postRepository.replyPost(replyPostRequest)

                if (response.code() == HttpURLConnection.HTTP_OK) {
                    responseResult.postValue(BaseResponse.Success(response.body()))
                } else {
                    val json = response.errorBody()?.string()
                    val errorObject = json.convertToErrorResponse()
                    responseResult.postValue(BaseResponse.Error(errorObject.message))
                }
            } catch (ex: Exception) {
                responseResult.postValue(BaseResponse.Error(ex.message))
            }
        }
    }
}
