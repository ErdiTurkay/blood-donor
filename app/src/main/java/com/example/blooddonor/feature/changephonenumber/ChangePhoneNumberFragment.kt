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
import com.example.blooddonor.feature.MainActivity
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
    private lateinit var activity: MainActivity

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
        activity = requireActivity() as MainActivity

        activity.binding.includeHeader.back.show()

        currentPhoneNumber = sessionManager.getUser().phone

        val countryCode = currentPhoneNumber.split(" ")[0]
        val afterCountryCode = currentPhoneNumber.split(" ")[1]

        binding.countryPicker.setCountryForPhoneCode(countryCode.toInt())
        binding.txtInputPhoneNumber.setText(afterCountryCode)

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
            changePhoneNumber()
        }

        return binding.root
    }

    private fun changePhoneNumber() {
        val countryCode = binding.countryPicker.selectedCountryCode
        val afterCountryCode = binding.txtInputPhoneNumber.text.toString()
        newPhoneNumber = "$countryCode $afterCountryCode"

        val isPhoneNumbersAreSame = currentPhoneNumber == newPhoneNumber
        val isPhoneNumberStartsWithZero = afterCountryCode.isNotEmpty() && afterCountryCode[0] == '0'
        val isLengthOfPhoneNumberTen = afterCountryCode.length == 10

        binding.error.text = if (isPhoneNumberStartsWithZero) {
            getString(R.string.phone_cannot_be_start_with_zero)
        } else if (isPhoneNumbersAreSame) {
            getString(R.string.phone_cannot_be_same)
        } else if (!isLengthOfPhoneNumberTen) {
            getString(R.string.phone_numbers_length_must_be_ten)
        } else {
            binding.error.text
        }

        isAvailable = afterCountryCode.isNotEmpty() && !isPhoneNumbersAreSame &&
            !isPhoneNumberStartsWithZero && isLengthOfPhoneNumberTen

        if (isAvailable) {
            viewModel.changePhoneNumber(newPhoneNumber)
        } else {
            binding.error.show()
        }
    }

    override fun onPause() {
        activity.binding.includeHeader.back.gone()
        super.onPause()
    }
}
