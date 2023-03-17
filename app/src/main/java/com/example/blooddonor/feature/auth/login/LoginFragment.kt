package com.example.blooddonor.feature.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.blooddonor.data.api.response.BaseResponse
import com.example.blooddonor.data.api.response.LoginResponse
import com.example.blooddonor.databinding.FragmentLoginBinding
import com.example.blooddonor.feature.MainActivity
import com.example.blooddonor.utils.* // ktlint-disable no-wildcard-imports
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var activity: MainActivity
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var greetingMessage: GreetingMessage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        activity = requireActivity() as MainActivity

        activity.binding.bottomNav.gone()
        activity.binding.includeHeader.root.gone()

        val token = sessionManager.getToken()
        if (!token.isNullOrBlank()) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
        }

        binding.timeTitle.text = greetingMessage.getTimeString()
        binding.timeImage.setImageDrawable(greetingMessage.getTimeDrawable())

        txtInputTextChange()

        binding.btnLogin.setOnClickListener {
            doLogin()
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        viewModel.loginResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {
                    binding.progress.show()
                }

                is BaseResponse.Success -> {
                    binding.progress.gone()
                    processLogin(it.data)
                }

                is BaseResponse.Error -> {
                    binding.progress.gone()
                    binding.errorInvalid.text = it.msg
                    binding.errorInvalid.show()
                }
            }
        }

        return binding.root
    }

    private fun txtInputTextChange() {
        binding.run {
            txtInputEmail.doAfterTextChanged {
                binding.errorEmail.showOrHide(it?.length == 0)
            }

            txtInputPassword.doAfterTextChanged {
                binding.errorPassword.showOrHide(it?.length == 0)
            }
        }
    }

    private fun doLogin() {
        val email = binding.txtInputEmail.text.toString()
        val password = binding.txtInputPassword.text.toString()

        binding.errorEmail.showOrHide(email.isEmpty())
        binding.errorPassword.showOrHide(password.isEmpty())

        val isAvailable = email.isNotEmpty() && password.isNotEmpty()

        if (isAvailable) {
            viewModel.loginUser(email = email, password = password)
        }
    }

    private fun processLogin(data: LoginResponse?) {
        data?.run {
            sessionManager.saveAuthToken(token)
            sessionManager.saveUser(user)

            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
        }
    }
}
