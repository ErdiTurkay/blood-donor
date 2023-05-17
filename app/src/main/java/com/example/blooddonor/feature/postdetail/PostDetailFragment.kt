package com.example.blooddonor.feature.postdetail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.blooddonor.R
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.data.model.Post
import com.example.blooddonor.data.model.fullLocation
import com.example.blooddonor.data.model.fullName
import com.example.blooddonor.databinding.FragmentPostDetailBinding
import com.example.blooddonor.feature.MainActivity
import com.example.blooddonor.utils.* // ktlint-disable no-wildcard-imports
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailFragment : Fragment() {
    private lateinit var binding: FragmentPostDetailBinding
    private lateinit var activity: MainActivity
    private val viewModel: PostDetailViewModel by viewModels()
    lateinit var post: Post

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPostDetailBinding.inflate(layoutInflater)
        activity = requireActivity() as MainActivity

        val navArgs by navArgs<PostDetailFragmentArgs>()
        post = navArgs.post.convertToPost()

        activity.binding.bottomNav.gone()

        activity.binding.includeHeader.back.show()
        activity.binding.includeHeader.headerTitle.text = getString(R.string.post_detail_title)

        setReplyRV()

        binding.btnSend.setOnClickListener {
            val isAvailable = !binding.txtInputNewComment.text.isNullOrEmpty()

            val postId = post.id
            val comment = binding.txtInputNewComment.text.toString()

            binding.errorNewComment.showOrHide(comment.isEmpty())

            if (isAvailable) {
                viewModel.replyPost(postId, comment)
                binding.txtInputNewComment.setText("")
            }
        }

        viewModel.responseResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {
                    binding.progress.show()
                }

                is BaseResponse.Success -> {
                    binding.progress.gone()

                    Snackbar.make(
                        requireView(),
                        getString(R.string.comment_sent),
                        Snackbar.LENGTH_LONG,
                    ).show()

                    post.replies = it.data?.post?.replies ?: post.replies

                    val replyAdapter = ReplyAdapter()
                    binding.replyRv.adapter = replyAdapter

                    replyAdapter.setReplyList(post.replies)
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
            callIntent.data = Uri.parse("tel:${post.user.phone}")
            startActivity(callIntent)
        }

        binding.wpButton.setOnClickListener {
            val packageManager = requireContext().packageManager
            val wpIntent = Intent(Intent.ACTION_VIEW)
            wpIntent.data = Uri.parse("https://wa.me/${post.user.phone}")

            if (wpIntent.resolveActivity(packageManager) != null) {
                startActivity(wpIntent)
            } else {
                Toast.makeText(context, "WhatsApp yüklü değil", Toast.LENGTH_SHORT).show()
            }
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
