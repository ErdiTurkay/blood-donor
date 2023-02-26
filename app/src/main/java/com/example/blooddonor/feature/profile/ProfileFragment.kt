package com.example.blooddonor.feature.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.blooddonor.R
import com.example.blooddonor.databinding.FragmentProfileBinding
import com.example.blooddonor.utils.SessionManager

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        binding.logout.run {
            rowText.text = "Çıkış Yap"
            root.setOnClickListener {
                SessionManager.clearData(requireContext())
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }
        }

        return binding.root
    }
}