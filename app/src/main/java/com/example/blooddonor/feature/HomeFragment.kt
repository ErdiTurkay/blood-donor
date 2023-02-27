package com.example.blooddonor.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.blooddonor.data.model.BloodAd
import com.example.blooddonor.databinding.FragmentHomeBinding
import com.example.blooddonor.utils.GreetingMessage
import com.example.blooddonor.utils.SessionManager
import com.example.blooddonor.utils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var activity: MainActivity
    // private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var greetingMessage: GreetingMessage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        activity = requireActivity() as MainActivity

        activity.binding.bottomNav.show()
        activity.binding.includeHeader.root.show()
        setHeaderTitle()

        setBloodAdRV()

        return binding.root
    }

    private fun setBloodAdRV() {
        val bloodAdAdapter = BloodAdAdapter()
        binding.bloodAdRv.adapter = bloodAdAdapter
        val bloodAdList = listOf(
            BloodAd("Ulaş Deniz Işık", 27, "A Rh+", "https://imgyukle.com/f/2023/02/25/QIHJqH.png"),
            BloodAd("Celal Şengör", 63, "A Rh+", "https://cdn.karar.com/news/1528527.jpg")
        )

        bloodAdAdapter.setBloodAdList(bloodAdList)
    }

    private fun setHeaderTitle() {
        activity.binding.includeHeader.headerTitle.text =
            greetingMessage.getTimeString()
                .plus("\n")
                .plus(sessionManager.getString(SessionManager.NAME))
    }
}