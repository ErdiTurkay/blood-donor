package com.erdi.blooddonor.feature.createnewpost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erdi.blooddonor.data.api.request.CreateNewPostRequest
import com.erdi.blooddonor.data.api.response.BaseResponse
import com.erdi.blooddonor.data.api.response.CreateNewPostResponse
import com.erdi.blooddonor.data.model.Location
import com.erdi.blooddonor.data.repository.PostRepository
import com.erdi.blooddonor.data.repository.UserRepository
import com.erdi.blooddonor.utils.FirebaseMethods
import com.erdi.blooddonor.utils.convertToErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNewPostViewModel @Inject constructor(
    var postRepository: PostRepository,
    var userRepository: UserRepository,
    var firebaseMethods: FirebaseMethods,
) : ViewModel() {
    val responseResult: MutableLiveData<BaseResponse<CreateNewPostResponse>> = MutableLiveData()
    val notificationResponse: MutableLiveData<String> = MutableLiveData()

    fun createNewPost(
        patientName: String,
        patientSurname: String,
        patientAge: Int,
        patientBloodType: String,
        patientLocation: Location,
        message: String,
    ) {
        responseResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val createNewPostRequest = CreateNewPostRequest(patientName, patientSurname, patientAge, patientBloodType, patientLocation, message)
                val response = postRepository.createNewPost(createNewPostRequest)

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

    fun sendNotificationToThisLocation(postId: String, patientName: String, patientBloodType: String, city: String, district: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userRepository.getNotificationTokens(city, district)

                if (response.isSuccessful) {
                    firebaseMethods.sendNewPostNotification(
                        tokenList = response.body()!!.tokens,
                        name = patientName,
                        bloodType = patientBloodType,
                        postId = postId,
                    )

                    notificationResponse.postValue(postId)
                }
            } catch (ex: Exception) {
            }
        }
    }
}
