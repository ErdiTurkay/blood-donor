package com.erdi.blooddonor.feature.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.erdi.blooddonor.data.api.response.AuthResponse
import com.erdi.blooddonor.data.api.response.BaseResponse
import com.erdi.blooddonor.databinding.FragmentLoginBinding
import com.erdi.blooddonor.feature.MainActivity
import com.erdi.blooddonor.utils.* // ktlint-disable no-wildcard-imports
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

        checkToken()

        activity.binding.run {
            bottomNav.gone()
            includeHeader.root.gone()
        }

        setTimeAndTimeDrawable()

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        binding.btnLogin.setOnClickListener {
            doLogin()
        }

        observeLoginResponse()
        txtInputTextChange()

        return binding.root
    }

    private fun checkToken() {
        val token = sessionManager.getToken()
        if (!token.isNullOrBlank()) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment(false))
        }
    }

    private fun setTimeAndTimeDrawable() {
        binding.run {
            timeTitle.text = greetingMessage.getTimeString()
            timeImage.setImageDrawable(greetingMessage.getTimeDrawable())
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

    private fun processLogin(data: AuthResponse?) {
        data?.run {
            sessionManager.run {
                saveUser(user)
                saveAuthToken(token)
                getNotificationToken()?.let {
                    viewModel.sendNotificationToken(it)
                }
            }

            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment(true))
        }
    }

    private fun observeLoginResponse() {
        viewModel.loginResult.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> binding.progress.show()

                is BaseResponse.Success -> {
                    binding.progress.gone()
                    processLogin(it.data)
                }

                is BaseResponse.Error -> {
                    binding.run {
                        progress.gone()
                        errorInvalid.text = it.msg
                        errorInvalid.show()
                    }
                }
            }
        }
    }

    private fun txtInputTextChange() {
        binding.run {
            txtInputEmail.doAfterTextChanged {
                errorEmail.showOrHide(it?.length == 0)
            }

            txtInputPassword.doAfterTextChanged {
                errorPassword.showOrHide(it?.length == 0)
            }
        }
    }
}
