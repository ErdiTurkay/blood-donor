package com.erdi.blooddonor.feature.postdetail

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import javax.inject.Inject

@AndroidEntryPoint
class PostDetailFragment : Fragment() {
    private lateinit var binding: FragmentPostDetailBinding
    private lateinit var activity: MainActivity
    private val viewModel: PostDetailViewModel by viewModels()
    lateinit var postId: String
    lateinit var post: Post

    @Inject
    lateinit var sessionManager: SessionManager

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPostDetailBinding.inflate(layoutInflater)
        activity = requireActivity() as MainActivity

        val navArgs by navArgs<PostDetailFragmentArgs>()
        postId = navArgs.postId

        activity.binding.run {
            bottomNav.gone()
            includeHeader.back.show()
            includeHeader.headerTitle.text = getString(R.string.post_detail_title)
        }

        viewModel.readPost(postId)

        observePostResponse()
        observeDeleteResponse()
        observeReplyResponse()

        binding.btnSend.setOnClickListener {
            sendReply()
        }

        binding.icRemove.setOnClickListener {
            showTwoOptionsDialog()
        }

        binding.callButton.setOnClickListener {
            callThisNumber(post.user.phone)
        }

        binding.wpButton.setOnClickListener {
            writeThisNumberOnWhatsApp(post.user.phone)
        }

        return binding.root
    }

    private fun callThisNumber(number: String) {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:+$number")
        startActivity(callIntent)
    }

    private fun writeThisNumberOnWhatsApp(number: String) {
        val url = "https://wa.me/${number.replace(" ", "")}"

        val openWhatsappIntent = Intent(Intent.ACTION_VIEW)
        openWhatsappIntent.data = Uri.parse(url)
        startActivity(openWhatsappIntent)
    }

    private fun showTwoOptionsDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle("UYARI")
        dialogBuilder.setMessage("İlanı silmek istediğine emin misin?\nBu işlemden sonra ilanın diğer kullanıcılar tarafından görüntülenemeyecektir.")
            .setPositiveButton(
                "Evet",
            ) { _, _ ->
                viewModel.deletePost(postId)
            }
            .setNegativeButton(
                "Hayır",
            ) { _, _ ->
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun setReplyRV() {
        val replyAdapter = ReplyAdapter()
        binding.replyRv.adapter = replyAdapter
        replyAdapter.setReplyList(post.replies)
    }

    private fun sendReply() {
        val isAvailable = !binding.txtInputNewComment.text.isNullOrEmpty()
        val comment = binding.txtInputNewComment.text.toString()

        binding.errorNewComment.showOrHide(comment.isEmpty())

        if (isAvailable) {
            viewModel.replyPost(postId, comment, post.user.notificationToken)
            binding.txtInputNewComment.setText("")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observePostResponse() {
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
                        binding.date.text =
                            createdAt.convertToLocalDateTime().convertToReadableDate()
                        binding.ownerName.text =
                            resources.getString(R.string.post_owner, user.fullName())

                        binding.icRemove.showOrHide(sessionManager.getUser().id == post.user.id)
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
                    binding.adIsRemoved.show()
                }
            }
        }
    }

    private fun observeDeleteResponse() {
        viewModel.deleteResponse.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {
                    binding.progress.show()
                    binding.postDetailLayout.hide()
                }

                is BaseResponse.Success -> {
                    binding.progress.gone()

                    Toast.makeText(requireContext(), "İlanınız başarıyla kaldırıldı.", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }

                is BaseResponse.Error -> {
                    binding.progress.gone()
                    binding.errorNewComment.text = it.msg
                    binding.errorNewComment.show()
                }
            }
        }
    }

    private fun observeReplyResponse() {
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
    }
}
