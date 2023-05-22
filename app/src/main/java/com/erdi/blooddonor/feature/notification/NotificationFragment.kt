package com.erdi.blooddonor.feature.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.erdi.blooddonor.R
import com.erdi.blooddonor.databinding.FragmentNotificationBinding
import com.erdi.blooddonor.feature.MainActivity

class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var activity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        activity = requireActivity() as MainActivity

        activity.binding.includeHeader.headerTitle.text = getString(R.string.notifications)

        return binding.root
    }
}
