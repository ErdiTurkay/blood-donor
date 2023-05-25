package com.erdi.blooddonor.feature.myposts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.erdi.blooddonor.R
import com.erdi.blooddonor.data.api.response.BaseResponse
import com.erdi.blooddonor.data.model.Post
import com.erdi.blooddonor.databinding.FragmentMyPostsBinding
import com.erdi.blooddonor.feature.MainActivity
import com.erdi.blooddonor.feature.home.BloodAdAdapter
import com.erdi.blooddonor.feature.home.PostClickListener
import com.erdi.blooddonor.utils.SessionManager
import com.erdi.blooddonor.utils.gone
import com.erdi.blooddonor.utils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyPostsFragment : Fragment(), PostClickListener {
    private lateinit var binding: FragmentMyPostsBinding
    private lateinit var activity: MainActivity
    private val viewModel: MyPostsViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyPostsBinding.inflate(layoutInflater)
        activity = requireActivity() as MainActivity

        activity.binding.run {
            bottomNav.show()
            includeHeader.root.show()
            includeHeader.back.gone()
            includeHeader.headerTitle.text = getString(R.string.my_posts)
        }

        setBloodAdRV()

        binding.fab.setOnClickListener {
            findNavController().navigate(MyPostsFragmentDirections.actionMyPostsFragmentToCreateNewPostFragment())
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getMyPosts()
            binding.swipeRefresh.isRefreshing = false
        }

        return binding.root
    }

    private fun setBloodAdRV() {
        val bloodAdAdapter = BloodAdAdapter(this, sessionManager.getUser())
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

                    if (postList.isEmpty()) {
                        binding.noAdText.show()
                    } else {
                        bloodAdAdapter.setBloodAdList(postList)
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
            findNavController().navigate(MyPostsFragmentDirections.actionMyPostsFragmentToPostDetailFragment(it))
        }
    }
}
