package com.erdi.blooddonor.feature.home

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
import com.erdi.blooddonor.databinding.FragmentHomeBinding
import com.erdi.blooddonor.feature.MainActivity
import com.erdi.blooddonor.utils.GreetingMessage
import com.erdi.blooddonor.utils.SessionManager
import com.erdi.blooddonor.utils.convertToJson
import com.erdi.blooddonor.utils.gone
import com.erdi.blooddonor.utils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), PostClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var activity: MainActivity
    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var greetingMessage: GreetingMessage

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        activity = requireActivity() as MainActivity

        activity.binding.bottomNav.show()
        activity.binding.includeHeader.root.show()
        activity.binding.includeHeader.back.gone()

        setHeaderTitle()
        setBloodAdRV()

        binding.fab.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCreateNewPostFragment())
        }

        return binding.root
    }

    private fun setBloodAdRV() {
        val bloodAdAdapter = BloodAdAdapter(this)
        binding.bloodAdRv.adapter = bloodAdAdapter
        var postList = emptyList<Post>()

        viewModel.postResponse.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {
                    binding.progress.show()
                }

                is BaseResponse.Success -> {
                    it.data?.posts?.let { posts ->
                        postList = posts
                    }

                    bloodAdAdapter.setBloodAdList(postList)
                    binding.progress.gone()
                }

                is BaseResponse.Error -> {
                    binding.progress.gone()
                    Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setHeaderTitle() {
        activity.binding.includeHeader.headerTitle.text = greetingMessage.getHeaderText()
    }

    override fun postOnClick(post: Post) {
        post.id.let {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPostDetailFragment(it))
        }
    }
}
