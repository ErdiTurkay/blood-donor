package com.example.blooddonor.feature.changephonenumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.blooddonor.NavGraphDirections
import com.example.blooddonor.R
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.databinding.FragmentChangePhoneNumberBinding
import com.example.blooddonor.utils.SessionManager
import com.example.blooddonor.utils.gone
import com.example.blooddonor.utils.show
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangePhoneNumberFragment : Fragment() {
    private lateinit var binding: FragmentChangePhoneNumberBinding
    private val viewModel: ChangePhoneNumberViewModel by viewModels()
    private var isAvailable: Boolean = false
    lateinit var currentPhoneNumber: String
    lateinit var newPhoneNumber: String

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentChangePhoneNumberBinding.inflate(layoutInflater)

        currentPhoneNumber = sessionManager.getUser().phone
        binding.txtInputPhoneNumber.setText(currentPhoneNumber)

        viewModel.responseResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {
                    binding.progress.show()
                }

                is BaseResponse.Success -> {
                    binding.progress.gone()

                    Snackbar.make(
                        requireView(),
                        getString(R.string.phone_number_is_succesfully_changed),
                        Snackbar.LENGTH_LONG,
                    ).show()

                    findNavController().navigate(NavGraphDirections.actionGlobalProfileFragment())

                    val newUser = sessionManager.getUser()
                    newUser.phone = newPhoneNumber
                    sessionManager.saveUser(newUser)
                }

                is BaseResponse.Error -> {
                    binding.progress.gone()
                    binding.error.text = it.msg
                    binding.error.show()
                }
            }
        }

        binding.btnChangePhoneNumber.setOnClickListener {
            newPhoneNumber = binding.txtInputPhoneNumber.text.toString()
            val phoneNumbersAreSame = currentPhoneNumber == newPhoneNumber
            if (phoneNumbersAreSame) {
                binding.error.text = getString(R.string.passwords_cannot_be_same)
                binding.error.show()
            }

            isAvailable = newPhoneNumber.isNotEmpty() && !phoneNumbersAreSame

            if (isAvailable) {
                viewModel.changePhoneNumber(newPhoneNumber)
            }
        }

        return binding.root
    }
}
