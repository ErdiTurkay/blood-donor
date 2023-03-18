package com.example.blooddonor.feature.auth.register

import androidx.lifecycle.ViewModel
import com.example.blooddonor.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    var userRepository: UserRepository,
) : ViewModel()
