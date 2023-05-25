package com.erdi.blooddonor.feature.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.erdi.blooddonor.R
import com.erdi.blooddonor.databinding.FragmentNotificationBinding
import com.erdi.blooddonor.feature.MainActivity
import com.erdi.blooddonor.utils.SessionManager
import com.erdi.blooddonor.utils.gone
import com.erdi.blooddonor.utils.hide
import com.erdi.blooddonor.utils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : Fragment(), NotificationClickListener {
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var activity: MainActivity

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        activity = requireActivity() as MainActivity

        activity.binding.run {
            includeHeader.back.show()
            includeHeader.headerTitle.text = getString(R.string.notifications)
            bottomNav.hide()
        }

        loadNotifications()

        binding.btnClearNotification.setOnClickListener {
            clearNotifications()
        }

        return binding.root
    }

    private fun loadNotifications() {
        val notificationList = sessionManager.getNotificationList()

        if (notificationList.size == 0) {
            binding.noNotificationText.show()
            binding.btnClearNotification.gone()
        } else {
            val notificationAdapter = NotificationAdapter(this)
            binding.notificationRv.adapter = notificationAdapter
            notificationAdapter.setNotificationList(notificationList)
        }
    }

    private fun clearNotifications() {
        val newList = sessionManager.clearNotificationsAndRefresh()
        val notificationAdapter = NotificationAdapter(this)
        binding.notificationRv.adapter = notificationAdapter
        notificationAdapter.setNotificationList(newList)

        binding.noNotificationText.show()
        binding.btnClearNotification.gone()
    }

    override fun notificationOnClick(postId: String) {
        postId.let {
            findNavController().navigate(NotificationFragmentDirections.actionNotificationFragmentToPostDetailFragment(it))
        }
    }
}
