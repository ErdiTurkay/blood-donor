package com.example.blooddonor.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.blooddonor.R
import com.example.blooddonor.databinding.FragmentProfileBinding
import com.example.blooddonor.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        binding.profileName.text = sessionManager.getFullName()
        binding.profileMail.text = sessionManager.getUser().email

        Glide.with(this)
            .load(sessionManager.getUser())
            .placeholder(R.drawable.person_placeholder)
            .fitCenter()
            .into(binding.image)

        binding.changePassword.run {
            rowIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_password))
            rowText.text = getString(R.string.change_password)
            root.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToChangePasswordFragment())
            }
        }

        binding.changePhone.run {
            rowIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_phone))
            rowText.text = getString(R.string.change_phone_number)
            root.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToChangePhoneNumberFragment())
            }
        }

        binding.logout.run {
            rowIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_exit))
            rowText.text = getString(R.string.exit)
            root.setOnClickListener {
                sessionManager.clearData()
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
            }
        }

        return binding.root
    }
}
