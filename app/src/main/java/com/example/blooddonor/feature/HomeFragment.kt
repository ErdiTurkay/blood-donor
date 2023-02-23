package com.example.blooddonor.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.blooddonor.databinding.FragmentHomeBinding
import com.example.blooddonor.utils.SessionManager
import com.example.blooddonor.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var activity: MainActivity
    // private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        activity = requireActivity() as MainActivity

        activity.binding.bottomNav.show()
        activity.binding.includeHeader.root.show()

        binding.name.text = "Hoşgeldin ${SessionManager.getString(requireContext(), SessionManager.NAME)}"
        binding.token.text = SessionManager.getToken(requireContext())

        binding.btnLogout.setOnClickListener {
            SessionManager.clearData(requireContext())
            activity.navigateToLogin()
        }

        return binding.root
    }
}