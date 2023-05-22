package com.erdi.blooddonor.feature.postdetail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.erdi.blooddonor.R
import com.erdi.blooddonor.data.api.response.BaseResponse
import com.erdi.blooddonor.data.model.Post
import com.erdi.blooddonor.data.model.fullLocation
import com.erdi.blooddonor.data.model.fullName
import com.erdi.blooddonor.databinding.FragmentPostDetailBinding
import com.erdi.blooddonor.feature.MainActivity
import com.erdi.blooddonor.utils.* // ktlint-disable no-wildcard-imports
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailFragment : Fragment() {
    private lateinit var binding: FragmentPostDetailBinding
    private lateinit var activity: MainActivity
    private val viewModel: PostDetailViewModel by viewModels()
    lateinit var postId: String
    lateinit var post: Post

    @SuppressLint("SetTextI18n", "QueryPermissionsNeeded")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPostDetailBinding.inflate(layoutInflater)
        activity = requireActivity() as MainActivity

        val navArgs by navArgs<PostDetailFragmentArgs>()
        postId = navArgs.postId

        activity.binding.bottomNav.gone()

        activity.binding.includeHeader.back.show()
        activity.binding.includeHeader.headerTitle.text = getString(R.string.post_detail_title)

        viewModel.readPost(postId)

        viewModel.postResponse.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {
                    binding.progress.show()
                    binding.postDetailLayout.hide()
                }

                is BaseResponse.Success -> {
                    binding.progress.gone()

                    it.data?.post?.let { post ->
                        this.post = post
                    }

                    post.run {
                        binding.fullName.text = "$patientName $patientSurname"
                        binding.bloodGroup.text = patientBloodType
                        binding.location.text = location.fullLocation()
                        binding.age.text = patientAge.toString()
                        binding.message.text = message
                        binding.date.text = createdAt.convertToLocalDateTime().convertToReadableDate()
                        binding.ownerName.text = resources.getString(R.string.post_owner, user.fullName())
                    }

                    Glide.with(this)
                        .load(post.createdAt)
                        .placeholder(R.drawable.person_placeholder)
                        .centerCrop()
                        .into(binding.image)

                    setReplyRV()
                    binding.postDetailLayout.show()
                }

                is BaseResponse.Error -> {
                    binding.progress.gone()
                    binding.errorNewComment.text = it.msg
                    binding.errorNewComment.show()
                }
            }
        }

        binding.btnSend.setOnClickListener {
            val isAvailable = !binding.txtInputNewComment.text.isNullOrEmpty()
            val comment = binding.txtInputNewComment.text.toString()

            binding.errorNewComment.showOrHide(comment.isEmpty())

            if (isAvailable) {
                Log.d("Fayırbeys", "Post sahibinin tokenı: ${post.user.notificationToken}")
                viewModel.replyPost(postId, comment, post.user.notificationToken)
                binding.txtInputNewComment.setText("")
            }
        }

        viewModel.responseResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {
                    binding.progress.show()
                }

                is BaseResponse.Success -> {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.comment_sent),
                        Snackbar.LENGTH_LONG,
                    ).show()

                    post.replies = it.data?.post?.replies ?: post.replies

                    val replyAdapter = ReplyAdapter()
                    binding.replyRv.adapter = replyAdapter

                    replyAdapter.setReplyList(post.replies)

                    binding.progress.gone()
                }

                is BaseResponse.Error -> {
                    binding.progress.gone()
                    binding.errorNewComment.text = it.msg
                    binding.errorNewComment.show()
                }
            }
        }

        binding.callButton.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:+${post.user.phone}")
            startActivity(callIntent)
        }

        binding.wpButton.setOnClickListener {
            val packageManager = requireContext().packageManager
            val whatsappIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:+${post.user.phone}"))
            whatsappIntent.setPackage("com.whatsapp")
            if (whatsappIntent.resolveActivity(packageManager) != null) {
                startActivity(whatsappIntent)
            } else {
                Toast.makeText(requireContext(), "WhatsApp yüklü değil", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun setReplyRV() {
        val replyAdapter = ReplyAdapter()
        binding.replyRv.adapter = replyAdapter
        replyAdapter.setReplyList(post.replies)
    }

    override fun onDetach() {
        activity.binding.includeHeader.back.gone()
        super.onDetach()
    }
}
