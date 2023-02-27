package com.example.blooddonor.feature.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.blooddonor.R
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.data.api.response.LoginResponse
import com.example.blooddonor.databinding.FragmentLoginBinding
import com.example.blooddonor.feature.MainActivity
import com.example.blooddonor.utils.GreetingMessage
import com.example.blooddonor.utils.SessionManager
import com.example.blooddonor.utils.hide
import com.example.blooddonor.utils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var activity: MainActivity
    private val viewModel: LoginViewModel by viewModels()
    private var isLoginEnable: Boolean = false

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var greetingMessage: GreetingMessage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        activity = requireActivity() as MainActivity

        activity.binding.bottomNav.hide()
        activity.binding.includeHeader.root.hide()

        val token = sessionManager.getToken()
        if (!token.isNullOrBlank()) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        inputChangeListener()

        binding.timeTitle.text = greetingMessage.getTimeString()
        binding.timeImage.setImageDrawable(greetingMessage.getTimeDrawable())

        viewModel.loginResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {
                    binding.progress.show()
                }

                is BaseResponse.Success -> {
                    binding.progress.hide()
                    processLogin(it.data)
                }

                is BaseResponse.Error -> {
                    binding.progress.hide()
                    processError(it.msg)
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            doLogin()
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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
        } else if (email == "2" && password == "2") {
            sessionManager.saveAuthToken("token")
            sessionManager.run {
                saveString(SessionManager.NAME, "Erdi")
                saveString(SessionManager.SURNAME, "Türkay")
                saveString(SessionManager.MAIL, "erditurkay@gmail.com")
            }

            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

            return
        }

        if (isLoginEnable) {
            viewModel.loginUser(email = email, password = password)
        }
    }

    private fun processLogin(data: LoginResponse?) {
        if (!data?.token.isNullOrEmpty()) {
            data?.let {
                sessionManager.saveAuthToken(it.token)
                sessionManager.run {
                    saveString(SessionManager.NAME, it.user.name)
                    saveString(SessionManager.SURNAME, it.user.surname)
                    saveString(SessionManager.MAIL, it.user.email)
                }

                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
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