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

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        binding.profileName.text =
            SessionManager.getString(requireContext(), SessionManager.NAME)
                .plus(" ")
                .plus(SessionManager.getString(requireContext(), SessionManager.SURNAME))

        binding.profileMail.text =
            SessionManager.getString(requireContext(), SessionManager.MAIL)

        binding.changePassword.run {
            rowIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_password))
            rowText.text = getString(R.string.change_password)
            root.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
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
                SessionManager.clearData(requireContext())
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }
        }

        return binding.root
    }
}