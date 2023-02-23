package com.example.blooddonor.feature.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.example.blooddonor.R
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.data.api.response.LoginResponse
import com.example.blooddonor.databinding.FragmentLoginBinding
import com.example.blooddonor.feature.MainActivity
import com.example.blooddonor.utils.GreetingMessage
import com.example.blooddonor.utils.SessionManager
import com.example.blooddonor.utils.hide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var activity: MainActivity
    private val viewModel: LoginViewModel by viewModels()
    private var isLoginEnable: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        activity = requireActivity() as MainActivity

        activity.binding.bottomNav.hide()
        activity.binding.includeHeader.root.hide()

        inputChangeListener()

        binding.timeTitle.text = GreetingMessage.getTimeString(requireContext())
        binding.timeImage.setImageDrawable(GreetingMessage.getTimeDrawable(requireContext()))

        viewModel.loginResult.observe(viewLifecycleOwner) {
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

        return binding.root
    }

    private fun inputChangeListener() {
        binding.run {
            txtInputEmail.doAfterTextChanged {
                if (it?.length == 0) {
                    errorEmail.visibility = View.VISIBLE
                    isLoginEnable = false
                } else {
                    errorEmail.visibility = View.INVISIBLE
                    isLoginEnable = true
                }
            }

            txtInputPassword.doAfterTextChanged {
                if (it?.length == 0) {
                    errorPassword.visibility = View.VISIBLE
                    isLoginEnable = false
                } else {
                    errorPassword.visibility = View.INVISIBLE
                    isLoginEnable = true
                }
            }
        }
    }

    private fun navigateToRegister() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.container, RegisterFragment())
            .addToBackStack("auth")
            .commit()
    }

    private fun doLogin() {
        val email = binding.txtInputEmail.text.toString()
        val password = binding.txtInputPassword.text.toString()

        if (email.isEmpty()) {
            binding.errorEmail.visibility = View.VISIBLE
        }

        if (password.isEmpty()) {
            binding.errorPassword.visibility = View.VISIBLE
        }

        // TODO: This is for development. It will be deleted later.
        if (email == "1" && password == "1") {
            viewModel.loginUser(email = "erditurkay@gmail.com", password = "12345678")
            return
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
                SessionManager.saveAuthToken(requireContext(), it.token)
                SessionManager.run {
                    saveString(requireContext(), NAME, it.user.name)
                    saveString(requireContext(), SURNAME, it.user.surname)
                }

                activity.navigateToHome()
            }
        }
    }

    private fun processError(message: String?) {
        showToast("Error: $message")
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}