package com.example.blooddonor.feature.auth

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.data.api.response.LoginResponse
import com.example.blooddonor.databinding.ActivityLoginBinding
import com.example.blooddonor.utils.GreetingMessage
import com.example.blooddonor.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    private var isLoginEnable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val token = SessionManager.getToken(this)
        if (!token.isNullOrBlank()) {
            navigateToActivity(LogoutActivity())
        }

        inputChangeListener()

        binding.signInTitle.text = GreetingMessage.getTime(this)

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
            navigateToActivity(RegisterActivity())
        }
    }

    private fun inputChangeListener() {
        binding.run {
            txtInputEmail.doAfterTextChanged {
                if (it?.length == 0) {
                    emailError.visibility = View.VISIBLE
                    isLoginEnable = false
                } else {
                    emailError.visibility = View.INVISIBLE
                    isLoginEnable = true
                }
            }

            txtPass.doAfterTextChanged {
                if (it?.length == 0) {
                    passwordError.visibility = View.VISIBLE
                    isLoginEnable = false
                } else {
                    passwordError.visibility = View.INVISIBLE
                    isLoginEnable = true
                }
            }
        }
    }

    private fun navigateToActivity(activity: Activity, finishThisActivity: Boolean? = false) {
        if (finishThisActivity == true) {
            finish()
        }

        val intent = Intent(this, activity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    private fun doLogin() {
        val email = binding.txtInputEmail.text.toString()
        val password = binding.txtPass.text.toString()

        if (email.isEmpty()) {
            binding.emailError.visibility = View.VISIBLE
        }

        if (password.isEmpty()) {
            binding.passwordError.visibility = View.VISIBLE
        }

        if (isLoginEnable) {
            viewModel.loginUser(email = email, password = password)
        }
    }

    private fun showLoading() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.progress.visibility = View.GONE
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
            navigateToActivity(LogoutActivity())
        }
    }

    private fun processError(message: String?) {
        showToast("Error: $message")
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
