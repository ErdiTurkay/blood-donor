package com.example.blooddonor.feature.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.blooddonor.databinding.ActivityLogoutBinding
import com.example.blooddonor.utils.SessionManager

class LogoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.name.text = "Hoşgeldin ${SessionManager.getString(this, SessionManager.NAME)}"
        binding.token.text = SessionManager.getToken(this)

        binding.btnLogout.setOnClickListener {
            SessionManager.clearData(this)
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        }
    }
}