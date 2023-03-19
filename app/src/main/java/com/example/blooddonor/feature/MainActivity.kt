package com.example.blooddonor.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.blooddonor.R
import com.example.blooddonor.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fragment transitions are provided according to the clicks in the Bottom Navigation.
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        binding.includeHeader.profile.setOnClickListener {
            navController.navigate(R.id.action_global_profileFragment)
        }

        binding.includeHeader.notification.setOnClickListener {
            navController.navigate(R.id.action_global_notificationFragment)
        }

        binding.includeHeader.back.setOnClickListener {
            navController.popBackStack()
        }
    }
}
