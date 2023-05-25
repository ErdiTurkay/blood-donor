package com.erdi.blooddonor.feature.changelocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.erdi.blooddonor.R
import com.erdi.blooddonor.data.api.response.BaseResponse
import com.erdi.blooddonor.data.model.City
import com.erdi.blooddonor.data.model.Location
import com.erdi.blooddonor.data.model.loadCitiesFromJson
import com.erdi.blooddonor.databinding.FragmentChangeLocationBinding
import com.erdi.blooddonor.utils.SessionManager
import com.erdi.blooddonor.utils.gone
import com.erdi.blooddonor.utils.hide
import com.erdi.blooddonor.utils.show
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangeLocationFragment : Fragment() {
    private lateinit var binding: FragmentChangeLocationBinding
    private val viewModel: ChangeLocationViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    private lateinit var newLocation: Location

    private var cities: Array<City>? = null
    private var cityNames: ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentChangeLocationBinding.inflate(layoutInflater)

        loadCitiesAndAssign()
        getLocationOfUser()
        setCitySpinner()
        setSpinnerClicks()

        binding.btnChangeLocation.setOnClickListener {
            changeLocation()
        }

        observeResponseResult()

        return binding.root
    }

    private fun loadCitiesAndAssign() {
        cities = loadCitiesFromJson(requireContext())
        cityNames = cities?.map { city -> city.name.substring(0, 1).uppercase() + city.name.substring(1) } as ArrayList<String>?
    }

    private fun getLocationOfUser() {
        sessionManager.getUser().location.run {
            binding.txtInputCity.setText(city)
            binding.txtInputDistrict.setText(district)

            setDistrictSpinner(city.lowercase())
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
                binding.txtInputDistrict.setText("")
                binding.errorInvalid.hide()
                setDistrictSpinner(binding.txtInputCity.text.toString().lowercase())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setDistrictSpinner(city: String) {
        val selectedCity = cities?.filter { it.name == city }
        val districts: ArrayList<String>? = selectedCity?.get(0)?.counties as ArrayList<String>?
        val districtNames: ArrayList<String>? = districts?.map { it.substring(0, 1).uppercase() + it.substring(1) } as ArrayList<String>?

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
                binding.errorInvalid.hide()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setSpinnerClicks() {
        binding.run {
            txtInputCity.setOnClickListener {
                binding.spinnerCity.performClick()
            }

            txtInputDistrict.setOnClickListener {
                binding.spinnerDistrict.performClick()
            }
        }
    }

    private fun changeLocation() {
        val city = binding.txtInputCity.text.toString()
        val district = binding.txtInputDistrict.text.toString()

        newLocation = Location(city, district)
        val currentLocation = sessionManager.getUser().location

        if (city == currentLocation.city && district == currentLocation.district) {
            binding.errorInvalid.text = getString(R.string.location_can_not_be_same)
            binding.errorInvalid.show()
        } else {
            viewModel.changeLocation(newLocation)
        }
    }

    private fun observeResponseResult() {
        viewModel.responseResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {
                    binding.progress.show()
                }

                is BaseResponse.Success -> {
                    binding.progress.gone()

                    Snackbar.make(
                        requireView(),
                        getString(R.string.location_succesfully_changed),
                        Snackbar.LENGTH_LONG,
                    ).show()

                    findNavController().popBackStack()

                    val newUser = sessionManager.getUser()
                    newUser.location = newLocation
                    sessionManager.saveUser(newUser)
                }

                is BaseResponse.Error -> {
                    binding.progress.gone()
                    binding.errorInvalid.text = it.msg
                    binding.errorInvalid.show()
                }
            }
        }
    }
}
