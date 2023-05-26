package com.erdi.blooddonor.feature.home.postinmydistrict

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.erdi.blooddonor.data.api.response.BaseResponse
import com.erdi.blooddonor.data.model.Post
import com.erdi.blooddonor.databinding.FragmentPostInMyDistrictBinding
import com.erdi.blooddonor.feature.home.BloodAdAdapter
import com.erdi.blooddonor.feature.home.HomeFragmentDirections
import com.erdi.blooddonor.feature.home.HomeViewModel
import com.erdi.blooddonor.feature.home.PostClickListener
import com.erdi.blooddonor.utils.SessionManager
import com.erdi.blooddonor.utils.availableBloodTypes
import com.erdi.blooddonor.utils.gone
import com.erdi.blooddonor.utils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PostInMyDistrictFragment : Fragment(), PostClickListener {
    private lateinit var binding: FragmentPostInMyDistrictBinding
    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPostInMyDistrictBinding.inflate(layoutInflater)

        setBloodAdRV()

        binding.switchBloodGroup.setOnCheckedChangeListener { _, isChecked ->
            val bloodAdAdapter = BloodAdAdapter(this, sessionManager.getUser())
            binding.bloodAdRv.adapter = bloodAdAdapter
            var myList = viewModel.inMyDistrictList

            if (isChecked) {
                myList = myList.filter { post ->
                    sessionManager.getUser().bloodType
                        .availableBloodTypes()
                        .contains(post.patientBloodType)
                }

                if (myList.isEmpty()) {
                    binding.noAdText.show()
                } else {
                    bloodAdAdapter.setBloodAdList(myList)
                    binding.noAdText.gone()
                }
            } else {
                if (myList.isEmpty()) {
                    binding.noAdText.show()
                } else {
                    bloodAdAdapter.setBloodAdList(myList)
                    binding.noAdText.gone()
                }
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getPostsInMyDistrict()
            binding.swipeRefresh.isRefreshing = false
        }

        viewModel.location.observe(viewLifecycleOwner) {
            binding.location.text = it.district
        }

        return binding.root
    }

    private fun setBloodAdRV() {
        val bloodAdAdapter = BloodAdAdapter(this, sessionManager.getUser())
        binding.bloodAdRv.adapter = bloodAdAdapter
        var postList = emptyList<Post>()

        viewModel.postInMyDistrictResponse.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {
                    binding.progress.show()
                }

                is BaseResponse.Success -> {
                    it.data?.posts?.let { posts ->
                        postList = posts
                    }

                    if (postList.isEmpty()) {
                        binding.noAdText.show()
                    } else {
                        bloodAdAdapter.setBloodAdList(postList)
                        binding.noAdText.gone()
                    }

                    binding.progress.gone()
                }

                is BaseResponse.Error -> {
                    binding.progress.gone()
                    Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun postOnClick(post: Post) {
        post.id.let {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToPostDetailFragment(
                    it,
                ),
            )
        }
    }
}
