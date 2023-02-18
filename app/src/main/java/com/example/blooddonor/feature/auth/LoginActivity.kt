package com.example.blooddonor.feature.auth

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.data.api.response.LoginResponse
import com.example.blooddonor.databinding.ActivityLoginBinding
import com.example.blooddonor.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val token = SessionManager.getToken(this)
        if (!token.isNullOrBlank()) {
            navigateToHome()
        }

        viewModel.loginResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    processLogin(it.data)
                }

                is BaseResponse.Error -> {
                    stopLoading()
                    processError(it.msg)
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            doLogin()
        }

        binding.btnRegister.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun navigateToHome() {
        finish()
        val intent = Intent(this, LogoutActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    fun doLogin() {
        val email = binding.txtInputEmail.text.toString()
        val password = binding.txtPass.text.toString()
        viewModel.loginUser(email = email, password = password)
    }

    private fun showLoading() {
        binding.prgbar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.prgbar.visibility = View.GONE
    }

    private fun processLogin(data: LoginResponse?) {
        if (!data?.token.isNullOrEmpty()) {
            data?.let {
                SessionManager.saveAuthToken(this, it.token)
                SessionManager.run {
                    saveString(this@LoginActivity, NAME, it.user.name)
                    saveString(this@LoginActivity, SURNAME, it.user.surname)
                }
            }
            navigateToHome()
        }
    }

    private fun processError(message: String?) {
        showToast("Error: $message")
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
