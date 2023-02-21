package com.example.blooddonor.feature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.view.View
import com.example.blooddonor.R
import com.example.blooddonor.databinding.ActivityMainBinding
import com.example.blooddonor.feature.auth.LoginFragment
import com.example.blooddonor.feature.profile.ProfileFragment
import com.example.blooddonor.utils.GreetingMessage
import com.example.blooddonor.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = SessionManager.getToken(this)
        if (!token.isNullOrBlank()) {
            navigateToHome()
        } else {
            navigateToLogin()
        }

        binding.includeHeader.notification.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, NotificationFragment())
                .addToBackStack("notification")
                .commit()
        }

        binding.includeHeader.profile.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, ProfileFragment())
                .addToBackStack("profile")
                .commit()
        }
    }

    fun navigateToLogin() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, LoginFragment())
            .commit()

        binding.includeHeader.root.visibility = View.GONE
    }

    fun navigateToHome() {
        binding.includeHeader.root.visibility = View.VISIBLE

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, HomeFragment())
            .commit()

        setHeaderTitle()
    }

    private fun setHeaderTitle() {
        binding.includeHeader.headerTitle.text =
            GreetingMessage.getTimeString(this)
                .plus("\n")
                .plus(SessionManager.getString(this, SessionManager.NAME))
    }
}