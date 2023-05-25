package com.erdi.blooddonor.feature.postdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erdi.blooddonor.data.api.request.ReplyPostRequest
import com.erdi.blooddonor.data.api.response.BaseResponse
import com.erdi.blooddonor.data.api.response.DeletePostResponse
import com.erdi.blooddonor.data.api.response.GetOnePostResponse
import com.erdi.blooddonor.data.api.response.ReplyPostResponse
import com.erdi.blooddonor.data.repository.PostRepository
import com.erdi.blooddonor.utils.FirebaseMethods
import com.erdi.blooddonor.utils.SessionManager
import com.erdi.blooddonor.utils.convertToErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    var postRepository: PostRepository,
    var sessionManager: SessionManager,
    var firebaseMethods: FirebaseMethods,
) : ViewModel() {
    val responseResult: MutableLiveData<BaseResponse<ReplyPostResponse>> = MutableLiveData()
    var postResponse: MutableLiveData<BaseResponse<GetOnePostResponse>> = MutableLiveData()
    var deleteResponse: MutableLiveData<BaseResponse<DeletePostResponse>> = MutableLiveData()

    fun readPost(postId: String) {
        postResponse.value = BaseResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = postRepository.getOnePost(postId)

                if (response.code() == HttpURLConnection.HTTP_OK) {
                    postResponse.postValue(BaseResponse.Success(response.body()))
                } else {
                    val json = response.errorBody()?.string()
                    val errorObject = json.convertToErrorResponse()
                    postResponse.postValue(BaseResponse.Error(errorObject.message))
                }
            } catch (ex: Exception) {
                postResponse.postValue(BaseResponse.Error(ex.message))
            }
        }
    }

    fun deletePost(postId: String) {
        deleteResponse.value = BaseResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = postRepository.deletePost(postId)

                if (response.code() == HttpURLConnection.HTTP_OK) {
                    deleteResponse.postValue(BaseResponse.Success(response.body()))
                } else {
                    val json = response.errorBody()?.string()
                    val errorObject = json.convertToErrorResponse()
                    deleteResponse.postValue(BaseResponse.Error(errorObject.message))
                }
            } catch (ex: Exception) {
                deleteResponse.postValue(BaseResponse.Error(ex.message))
            }
        }
    }

    fun replyPost(postId: String, comment: String, authorToken: String? = "") {
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

                if (!authorToken.isNullOrEmpty()) {
                    firebaseMethods.sendReplyNotification(
                        token = authorToken,
                        name = sessionManager.getFullName(),
                        comment = comment,
                        postId = postId,
                    )
                }
            } catch (ex: Exception) {
                responseResult.postValue(BaseResponse.Error(ex.message))
            }
        }
    }
}
