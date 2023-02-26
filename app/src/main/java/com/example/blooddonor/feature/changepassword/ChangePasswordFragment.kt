package com.example.blooddonor.feature.changepassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.blooddonor.databinding.FragmentChangePasswordBinding
import com.example.blooddonor.feature.auth.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)

        inputChangeListener()

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
        }

        return binding.root
    }

    private fun inputChangeListener() {
        binding.run {
            txtInputCurrentPassword.doAfterTextChanged {
                if (it?.length == 0) {
                    errorCurrentPassword.visibility = View.VISIBLE
                } else {
                    errorCurrentPassword.visibility = View.INVISIBLE
                }
            }

            txtInputNewPassword.doAfterTextChanged {
                if (it?.length == 0) {
                    errorNewPassword.visibility = View.VISIBLE
                } else {
                    errorNewPassword.visibility = View.INVISIBLE
                }
            }

            txtInputNewPasswordConfirm.doAfterTextChanged {
                if (it?.length == 0) {
                    errorNewPasswordConfirm.visibility = View.VISIBLE
                } else {
                    errorNewPasswordConfirm.visibility = View.INVISIBLE
                }
            }
        }
    }

}