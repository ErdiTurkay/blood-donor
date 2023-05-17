package com.example.blooddonor.feature.createnewpost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooddonor.data.api.request.CreateNewPostRequest
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.data.api.response.CreateNewPostResponse
import com.example.blooddonor.data.model.Location
import com.example.blooddonor.data.repository.PostRepository
import com.example.blooddonor.utils.convertToErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNewPostViewModel @Inject constructor(
    var postRepository: PostRepository,
) : ViewModel() {
    val responseResult: MutableLiveData<BaseResponse<CreateNewPostResponse>> = MutableLiveData()

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
}
