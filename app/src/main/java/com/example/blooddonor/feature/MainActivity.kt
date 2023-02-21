package com.example.blooddonor.feature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.blooddonor.R
import com.example.blooddonor.databinding.ActivityMainBinding
import com.example.blooddonor.feature.auth.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigateToLogin()
    }

    fun navigateToLogin() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, LoginFragment())
            .commit()
    }
}