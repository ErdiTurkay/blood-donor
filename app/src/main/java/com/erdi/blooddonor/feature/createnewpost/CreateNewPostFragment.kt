package com.erdi.blooddonor.feature.createnewpost

import android.os.Bundle
import android.util.Log
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
import com.erdi.blooddonor.data.api.response.BaseResponse
import com.erdi.blooddonor.data.model.City
import com.erdi.blooddonor.data.model.Location
import com.erdi.blooddonor.data.model.loadCitiesFromJson
import com.erdi.blooddonor.databinding.FragmentCreateNewPostBinding
import com.erdi.blooddonor.feature.MainActivity
import com.erdi.blooddonor.utils.FirebaseMethods
import com.erdi.blooddonor.utils.camelCase
import com.erdi.blooddonor.utils.gone
import com.erdi.blooddonor.utils.show
import com.erdi.blooddonor.utils.showOrHide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateNewPostFragment : Fragment() {
    private lateinit var binding: FragmentCreateNewPostBinding
    private lateinit var activity: MainActivity
    private val viewModel: CreateNewPostViewModel by viewModels()

    private var cities: Array<City>? = null
    private var cityNames: ArrayList<String>? = null

    @Inject
    lateinit var firebaseMethods: FirebaseMethods

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCreateNewPostBinding.inflate(layoutInflater)
        activity = requireActivity() as MainActivity

        activity.binding.run {
            bottomNav.gone()
            includeHeader.back.show()
            includeHeader.headerTitle.text = getString(R.string.create_new_post)
        }

        loadCitiesAndAssign()
        setBloodTypeSpinner()
        setCitySpinner()
        txtInputTextChange()
        setSpinnerClicks()

        binding.btnCreateNewPost.setOnClickListener {
            createNewPost()
        }

        viewModel.notificationResponse.observe(viewLifecycleOwner) {
            findNavController().navigate(CreateNewPostFragmentDirections.actionCreateNewPostFragmentToPostDetailFragment(it))
        }

        observeResponseResult()

        return binding.root
    }

    private fun loadCitiesAndAssign() {
        cities = loadCitiesFromJson(requireContext())
        cityNames = cities?.map { city -> city.name.substring(0, 1).uppercase() + city.name.substring(1) } as ArrayList<String>?
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

    private fun createNewPost() {
        val name = binding.txtInputName.text.toString().camelCase()
        val surname = binding.txtInputSurname.text.toString().camelCase()
        val age = binding.txtInputAge.text.toString()
        val bloodType = binding.txtInputBloodGroup.text.toString()
        val city = binding.txtInputCity.text.toString()
        val district = binding.txtInputDistrict.text.toString()
        val message = binding.txtInputMessage.text.toString()

        val isAvailable = name.isNotEmpty() && surname.isNotEmpty() &&
            age.isNotEmpty() && bloodType.isNotEmpty() && city.isNotEmpty() &&
            district.isNotEmpty() && message.isNotEmpty()

        binding.run {
            errorName.showOrHide(name.isEmpty())
            errorSurname.showOrHide(surname.isEmpty())
            errorAge.showOrHide(age.isEmpty())
            errorBloodGroup.showOrHide(bloodType.isEmpty())
            errorCity.showOrHide(city.isEmpty())
            errorDistrict.showOrHide(district.isEmpty())
            errorMessage.showOrHide(message.isEmpty())
        }

        if (isAvailable) {
            viewModel.createNewPost(
                patientName = name,
                patientSurname = surname,
                patientAge = age.toInt(),
                patientBloodType = bloodType,
                patientLocation = Location(city, district),
                message = message,
            )
        }
    }

    private fun txtInputTextChange() {
        binding.run {
            txtInputName.doAfterTextChanged {
                errorName.showOrHide(it?.length == 0)
            }

            txtInputSurname.doAfterTextChanged {
                errorSurname.showOrHide(it?.length == 0)
            }

            txtInputAge.doAfterTextChanged {
                errorAge.showOrHide(it?.length == 0)
            }

            txtInputBloodGroup.doAfterTextChanged {
                errorBloodGroup.showOrHide(it?.length == 0)
            }

            txtInputCity.doAfterTextChanged {
                errorCity.showOrHide(it?.length == 0)
            }

            txtInputDistrict.doAfterTextChanged {
                errorDistrict.showOrHide(it?.length == 0)
            }

            txtInputMessage.doAfterTextChanged {
                errorMessage.showOrHide(it?.length == 0)
            }
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
                        getString(R.string.post_is_succesfully_created),
                        Snackbar.LENGTH_LONG,
                    ).show()

                    it.data?.post?.let { post ->
                        viewModel.sendNotificationToThisLocation(
                            post.id,
                            post.patientName,
                            post.patientBloodType,
                            post.location.city,
                            post.location.district,
                        )
                    }
                }

                is BaseResponse.Error -> {
                    binding.progress.gone()
                    /*binding.errorInvalid.text = it.msg
                    binding.errorInvalid.show()*/
                }
            }
        }
    }
}
