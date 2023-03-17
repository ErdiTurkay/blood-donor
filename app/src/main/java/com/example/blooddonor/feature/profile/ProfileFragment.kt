package com.example.blooddonor.feature.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        binding.profileName.text = sessionManager.getFullName()

        binding.profileMail.text = sessionManager.getUser().email

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
                // TODO: Yönlendirme yapılacak.
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