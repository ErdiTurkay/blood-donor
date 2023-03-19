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
import com.example.blooddonor.utils.convertToLocalDateTime
import com.example.blooddonor.utils.convertToPost
import com.example.blooddonor.utils.convertToReadableDate

class PostDetailFragment : Fragment() {
    private lateinit var binding: FragmentPostDetailBinding
    lateinit var post: Post

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPostDetailBinding.inflate(layoutInflater)

        val navArgs by navArgs<PostDetailFragmentArgs>()
        post = navArgs.post.convertToPost()

        binding.fullName.text = post.user.fullName()
        binding.bloodGroup.text = post.bloodType
        binding.location.text = post.location.fullLocation()
        binding.age.text = post.user.age().toString()
        binding.message.text = post.message
        binding.date.text = post.createdAt.convertToLocalDateTime().convertToReadableDate()

        Glide.with(this)
            .load("https://imgrosetta.mynet.com.tr/file/16648974/16648974-728xauto.jpg")
            .placeholder(R.drawable.celalsengor)
            .centerCrop()
            .into(binding.image)

        return binding.root
    }
}
