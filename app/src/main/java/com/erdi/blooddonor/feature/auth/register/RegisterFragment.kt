package com.erdi.blooddonor.feature.auth.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.erdi.blooddonor.R
import com.erdi.blooddonor.data.api.response.AuthResponse
import com.erdi.blooddonor.data.api.response.BaseResponse
import com.erdi.blooddonor.data.model.City
import com.erdi.blooddonor.data.model.Location
import com.erdi.blooddonor.data.model.loadCitiesFromJson
import com.erdi.blooddonor.databinding.FragmentRegisterBinding
import com.erdi.blooddonor.utils.SessionManager
import com.erdi.blooddonor.utils.convertToDate
import com.erdi.blooddonor.utils.gone
import com.erdi.blooddonor.utils.show
import com.erdi.blooddonor.utils.showErrorOrHide
import com.erdi.blooddonor.utils.showOrHide
import com.erdi.blooddonor.utils.sortAlphabetically
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    private var cities: Array<City>? = null
    private var cityNames: ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        loadCitiesAndAssign()
        setBloodTypeSpinner()
        setBirthdaySpinner()
        setLastDonationSpinner()
        setCitySpinner()
        setSpinnerClicks()

        binding.btnLogin.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnRegister.setOnClickListener {
            doRegister()
        }

        txtInputTextChange()
        observeResponseResult()

        return binding.root
    }

    private fun loadCitiesAndAssign() {
        cities = loadCitiesFromJson(requireContext())
        cityNames = cities?.map { city -> city.name.substring(0, 1).uppercase() + city.name.substring(1) } as ArrayList<String>?
        cityNames?.sortAlphabetically()
    }

    private fun setBloodTypeSpinner() {
        binding.txtInputBloodGroup.setOnClickListener {
            binding.spinnerBloodGroup.performClick()
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.blood_groups,
            android.R.layout.simple_spinner_item,
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerBloodGroup.adapter = adapter
        }

        val firstPosition: Int = binding.spinnerBloodGroup.selectedItemPosition
        binding.spinnerBloodGroup.setSelection(firstPosition, true)

        binding.spinnerBloodGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                binding.txtInputBloodGroup.setText(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setCitySpinner() {
        cityNames?.add(0, binding.txtInputCity.hint.toString() + " seçiniz...")

        val cityAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            cityNames?.toArray() as Array<out Any>,
        )

        binding.spinnerCity.adapter = cityAdapter

        val firstPosition: Int = binding.spinnerCity.selectedItemPosition
        binding.spinnerCity.setSelection(firstPosition, true)

        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                binding.txtInputCity.setText(selectedItem)
                setDistrictSpinner(binding.txtInputCity.text.toString().lowercase())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setDistrictSpinner(city: String) {
        val selectedCity = cities?.filter { it.name == city }
        val districts: ArrayList<String>? = selectedCity?.get(0)?.counties as ArrayList<String>?
        val districtNames: ArrayList<String>? = districts?.map { it.substring(0, 1).uppercase() + it.substring(1) } as ArrayList<String>?

        districtNames?.sortAlphabetically()

        districtNames?.add(0, binding.txtInputDistrict.hint.toString() + " seçiniz...")

        val districtAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            districtNames?.toArray() as Array<out Any>,
        )

        binding.spinnerDistrict.adapter = districtAdapter

        val firstPosition: Int = binding.spinnerDistrict.selectedItemPosition
        binding.spinnerDistrict.setSelection(firstPosition, true)

        binding.spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                binding.txtInputDistrict.setText(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setBirthdaySpinner() {
        binding.txtInputBirthday.setOnClickListener {
            showDatePickerDialog { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                binding.txtInputBirthday.setText(formattedDate)
            }
        }
    }

    private fun setLastDonationSpinner() {
        binding.txtInputLastDonation.setOnClickListener {
            showDatePickerDialog { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                binding.txtInputLastDonation.setText(formattedDate)
            }
        }
    }

    private fun setSpinnerClicks() {
        binding.run {
            txtInputCity.setOnClickListener {
                spinnerCity.performClick()
            }

            txtInputDistrict.setOnClickListener {
                spinnerDistrict.performClick()
            }
        }
    }

    private fun observeResponseResult() {
        viewModel.registerResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {
                    binding.progress.show()
                }

                is BaseResponse.Success -> {
                    binding.progress.gone()
                    processLogin(it.data)
                }

                is BaseResponse.Error -> {
                    binding.progress.gone()
                    binding.errorInvalid.text = it.msg
                    binding.errorInvalid.show()
                }
            }
        }
    }

    private fun doRegister() {
        val name = binding.txtInputName.text.toString()
        val surname = binding.txtInputSurname.text.toString()
        val mail = binding.txtInputEmail.text.toString()
        val password = binding.txtInputPassword.text.toString()
        val bloodType = binding.txtInputBloodGroup.text.toString()

        val countryCode = binding.countryPicker.selectedCountryCode
        val afterCountryCode = binding.txtInputPhoneNumber.text.toString()
        val phone = "$countryCode $afterCountryCode"

        val birthday = binding.txtInputBirthday.text.toString()
        val lastDonation = binding.txtInputLastDonation.text.toString()

        val city = binding.txtInputCity.text.toString()
        val district = binding.txtInputDistrict.text.toString()

        val isPhoneNumberStartsWithZero = afterCountryCode.isNotEmpty() && afterCountryCode[0] == '0'
        val isLengthOfPhoneNumberTen = afterCountryCode.length == 10

        binding.errorPhoneNumber.text = if (isPhoneNumberStartsWithZero) {
            getString(R.string.phone_cannot_be_start_with_zero)
        } else if (!isLengthOfPhoneNumberTen) {
            getString(R.string.phone_numbers_length_must_be_ten)
        } else {
            binding.errorPhoneNumber.text
        }

        val isAvailable = name.isNotEmpty() && surname.isNotEmpty() &&
            mail.isNotEmpty() && password.isNotEmpty() &&
            bloodType.isNotEmpty() && afterCountryCode.isNotEmpty() &&
            city.isNotEmpty() && district.isNotEmpty()

        binding.run {
            errorEmail.showErrorOrHide()
            errorPassword.showErrorOrHide()
            errorName.showErrorOrHide()
            errorSurname.showErrorOrHide()
            errorBirthday.showErrorOrHide()
            errorPhoneNumber.showOrHide(afterCountryCode.isEmpty() || isPhoneNumberStartsWithZero || !isLengthOfPhoneNumberTen)
            errorLastDonation.showErrorOrHide()
            errorBloodGroup.showErrorOrHide()
            errorCity.showErrorOrHide()
            errorDistrict.showErrorOrHide()
        }

        if (isAvailable) {
            viewModel.registerUser(
                name = name,
                surname = surname,
                email = mail,
                password = password,
                bloodType = bloodType,
                phone = phone,
                location = Location(city, district),
                dateOfBirth = birthday.convertToDate(),
                lastDonation = lastDonation.convertToDate(),
            )
        }
    }

    private fun processLogin(data: AuthResponse?) {
        data?.run {
            sessionManager.run {
                saveUser(user)
                saveAuthToken(token)
                getNotificationToken()?.let {
                    viewModel.sendNotificationToken(it)
                }
            }

            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToHomeFragment(true))
        }
    }

    private fun txtInputTextChange() {
        binding.run {
            txtInputEmail.doAfterTextChanged {
                errorEmail.showErrorOrHide()
            }

            txtInputPassword.doAfterTextChanged {
                errorPassword.showErrorOrHide()
            }

            txtInputName.doAfterTextChanged {
                errorName.showErrorOrHide()
            }

            txtInputSurname.doAfterTextChanged {
                errorSurname.showErrorOrHide()
            }

            txtInputBirthday.doAfterTextChanged {
                errorBirthday.showErrorOrHide()
            }

            txtInputPhoneNumber.doAfterTextChanged {
                errorPhoneNumber.showErrorOrHide()
            }

            txtInputLastDonation.doAfterTextChanged {
                errorLastDonation.showErrorOrHide()
            }

            txtInputBloodGroup.doAfterTextChanged {
                errorBloodGroup.showErrorOrHide()
            }

            txtInputCity.doAfterTextChanged {
                errorCity.showErrorOrHide()
            }

            txtInputDistrict.doAfterTextChanged {
                errorDistrict.showErrorOrHide()
            }
        }
    }

    private fun showDatePickerDialog(listener: DatePickerDialog.OnDateSetListener) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), listener, year, month, day)
        datePickerDialog.show()
    }
}
