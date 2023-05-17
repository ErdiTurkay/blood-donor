package com.example.blooddonor.feature.createnewpost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.blooddonor.databinding.FragmentCreateNewPostBinding
import com.example.blooddonor.feature.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateNewPostFragment : Fragment() {
    private lateinit var binding: FragmentCreateNewPostBinding
    private lateinit var activity: MainActivity
    private val viewModel: CreateNewPostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCreateNewPostBinding.inflate(layoutInflater)

        return binding.root
    }
}
