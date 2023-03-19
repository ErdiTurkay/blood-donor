package com.example.blooddonor.feature.postdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.blooddonor.R
import com.example.blooddonor.data.model.Post
import com.example.blooddonor.data.model.age
import com.example.blooddonor.data.model.fullLocation
import com.example.blooddonor.data.model.fullName
import com.example.blooddonor.databinding.FragmentPostDetailBinding
import com.example.blooddonor.feature.MainActivity
import com.example.blooddonor.utils.* // ktlint-disable no-wildcard-imports

class PostDetailFragment : Fragment() {
    private lateinit var binding: FragmentPostDetailBinding
    private lateinit var activity: MainActivity
    lateinit var post: Post

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPostDetailBinding.inflate(layoutInflater)
        activity = requireActivity() as MainActivity

        val navArgs by navArgs<PostDetailFragmentArgs>()
        post = navArgs.post.convertToPost()

        activity.binding.includeHeader.back.show()
        activity.binding.includeHeader.headerTitle.text = getString(R.string.post_detail_title)

        post.run {
            binding.fullName.text = user.fullName()
            binding.bloodGroup.text = bloodType
            binding.location.text = location.fullLocation()
            binding.age.text = user.age().toString()
            binding.message.text = message
            binding.date.text = createdAt.convertToLocalDateTime().convertToReadableDate()
        }

        Glide.with(this)
            .load("https://imgrosetta.mynet.com.tr/file/16648974/16648974-728xauto.jpg")
            .placeholder(R.drawable.person_placeholder)
            .centerCrop()
            .into(binding.image)

        return binding.root
    }

    override fun onDetach() {
        activity.binding.includeHeader.back.gone()
        super.onDetach()
    }
}
