package com.erdi.blooddonor.feature.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.erdi.blooddonor.databinding.FragmentHomeBinding
import com.erdi.blooddonor.feature.MainActivity
import com.erdi.blooddonor.utils.GreetingMessage
import com.erdi.blooddonor.utils.SessionManager
import com.erdi.blooddonor.utils.gone
import com.erdi.blooddonor.utils.show
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var activity: MainActivity
    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var greetingMessage: GreetingMessage

    @Inject
    lateinit var sessionManager: SessionManager

    var tabTitle = arrayOf("Tüm İlanlar", "Aynı İl", "Aynı İlçe")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        activity = requireActivity() as MainActivity
        val navArgs by navArgs<HomeFragmentArgs>()

        val afterAuth = navArgs.afterAuth

        setupViewPager()

        activity.binding.run {
            bottomNav.show()
            includeHeader.root.show()
            includeHeader.back.gone()
        }

        if (afterAuth) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModel.sendNotificationToken(task.result)
                    //Toast.makeText(requireContext(), task.result, Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("Firebase Token", "Token alınamadı: ${task.exception?.message}")
                }
            }
        }

        setHeaderTitle()

        binding.fab.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCreateNewPostFragment())
        }

        return binding.root
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) {
                tab, position ->
            tab.text = tabTitle[position]
        }.attach()
    }

    private fun setHeaderTitle() {
        activity.binding.includeHeader.headerTitle.text = greetingMessage.getHeaderText()
    }
}
