package com.example.blooddonor.feature.changepassword

import androidx.lifecycle.ViewModel
import com.example.blooddonor.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    var userRepository: UserRepository
): ViewModel() {
}