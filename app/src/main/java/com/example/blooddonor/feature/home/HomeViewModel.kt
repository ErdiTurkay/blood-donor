package com.example.blooddonor.feature.home

import androidx.lifecycle.ViewModel
import com.example.blooddonor.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    var userRepository: UserRepository,
) : ViewModel()
