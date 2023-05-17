package com.example.blooddonor.feature.createnewpost

import androidx.lifecycle.ViewModel
import com.example.blooddonor.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateNewPostViewModel @Inject constructor(
    var postRepository: PostRepository,
) : ViewModel()
