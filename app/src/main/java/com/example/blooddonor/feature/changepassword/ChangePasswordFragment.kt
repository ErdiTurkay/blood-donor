package com.example.blooddonor.feature.changepassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.blooddonor.R
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.databinding.FragmentChangePasswordBinding
import com.example.blooddonor.feature.auth.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding
    private val viewModel: ChangePasswordViewModel by viewModels()
    private var isAvailable: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)

        inputChangeListener()

        viewModel.responseResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {

                }

                is BaseResponse.Success -> {
                    findNavController().navigate(R.id.action_global_profileFragment)
                }

                is BaseResponse.Error -> {
                    Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnChangePassword.setOnClickListener {
            val currentPassword = binding.txtInputCurrentPassword.text.toString()
            val newPassword = binding.txtInputNewPassword.text.toString()
            val newPasswordConfirm = binding.txtInputNewPasswordConfirm.text.toString()

            if (currentPassword.isEmpty()) {
                binding.errorCurrentPassword.visibility = View.VISIBLE
            }

            if (newPassword.isEmpty()) {
                binding.errorNewPassword.visibility = View.VISIBLE
            }

            if (newPasswordConfirm.isEmpty()) {
                binding.errorNewPasswordConfirm.visibility = View.VISIBLE
            }

            if (newPassword != newPasswordConfirm) {
                binding.errorNewPassword.text = "Girilen şifreler aynı olmalıdır."
                binding.errorNewPasswordConfirm.text = "Girilen şifreler aynı olmalıdır."
                binding.errorNewPassword.visibility = View.VISIBLE
                binding.errorNewPasswordConfirm.visibility = View.VISIBLE
            }

            if (isAvailable) {
                viewModel.changePassword(newPassword)
            }
        }

        return binding.root
    }

    private fun inputChangeListener() {
        binding.run {
            txtInputCurrentPassword.doAfterTextChanged {
                if (it?.length == 0) {
                    errorCurrentPassword.visibility = View.VISIBLE
                    isAvailable = false
                } else {
                    errorCurrentPassword.visibility = View.INVISIBLE
                    isAvailable = true
                }
            }

            txtInputNewPassword.doAfterTextChanged {
                if (it?.length == 0) {
                    errorNewPassword.visibility = View.VISIBLE
                    isAvailable = false
                } else {
                    errorNewPassword.visibility = View.INVISIBLE
                    isAvailable = true
                }
            }

            txtInputNewPasswordConfirm.doAfterTextChanged {
                if (it?.length == 0) {
                    errorNewPasswordConfirm.visibility = View.VISIBLE
                    isAvailable = false
                } else {
                    errorNewPasswordConfirm.visibility = View.INVISIBLE
                    isAvailable = true
                }
            }
        }
    }

}