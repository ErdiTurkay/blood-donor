package com.example.blooddonor.feature.changepassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.blooddonor.R
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.databinding.FragmentChangePasswordBinding
import com.example.blooddonor.utils.gone
import com.example.blooddonor.utils.show
import com.example.blooddonor.utils.showOrHide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding
    private val viewModel: ChangePasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)

        txtInputTextChangeListener()

        binding.btnChangePassword.setOnClickListener {
            changePassword()
        }

        observeResponseResult()

        return binding.root
    }

    private fun observeResponseResult() {
        viewModel.responseResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> binding.progress.show()

                is BaseResponse.Success -> {
                    binding.progress.gone()

                    Snackbar.make(
                        requireView(),
                        R.string.password_change_successful,
                        Snackbar.LENGTH_LONG,
                    ).show()

                    findNavController().popBackStack()
                }

                is BaseResponse.Error -> {
                    binding.progress.gone()
                    binding.errorCurrentPassword.text = it.msg
                    binding.errorCurrentPassword.show()
                }
            }
        }
    }

    private fun changePassword() {
        val currentPassword = binding.txtInputCurrentPassword.text.toString()
        val newPassword = binding.txtInputNewPassword.text.toString()
        val newPasswordConfirm = binding.txtInputNewPasswordConfirm.text.toString()

        val listOfInput = listOf(currentPassword, newPassword, newPasswordConfirm)

        val isAvailable = listOfInput.all { it.isNotEmpty() } && newPassword == newPasswordConfirm

        binding.errorCurrentPassword.showOrHide(currentPassword.isEmpty())
        binding.errorNewPassword.showOrHide(
            newPassword.isEmpty() || newPassword != newPasswordConfirm,
        )
        binding.errorNewPasswordConfirm.showOrHide(
            newPasswordConfirm.isEmpty() || newPassword != newPasswordConfirm,
        )

        if (isAvailable) {
            viewModel.changePassword(currentPassword, newPassword)
        }
    }

    private fun txtInputTextChangeListener() {
        binding.run {
            txtInputCurrentPassword.doAfterTextChanged {
                errorCurrentPassword.showOrHide(it?.length == 0)
            }

            txtInputNewPassword.doAfterTextChanged {
                errorNewPassword.showOrHide(it?.length == 0)
            }

            txtInputNewPasswordConfirm.doAfterTextChanged {
                errorNewPasswordConfirm.showOrHide(it?.length == 0)
            }
        }
    }
}
