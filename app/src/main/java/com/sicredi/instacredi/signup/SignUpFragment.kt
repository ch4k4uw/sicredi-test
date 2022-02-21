package com.sicredi.instacredi.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sicredi.instacredi.R
import com.sicredi.instacredi.common.extensions.disable
import com.sicredi.instacredi.common.extensions.enable
import com.sicredi.instacredi.common.extensions.gone
import com.sicredi.instacredi.common.extensions.sText
import com.sicredi.instacredi.common.extensions.visible
import com.sicredi.instacredi.databinding.FragmentSignUpBinding
import com.sicredi.instacredi.signup.interaction.SignUpState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private val viewModel by viewModels<SignUpViewModel>()
    private lateinit var viewBinding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSignUpBinding.inflate(inflater, container, false).let {
        it.root.apply { viewBinding = it }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        with(viewBinding) {
            toolbar.setNavigationOnClickListener { navigateUp() }
            name.addTextChangedListener { nameInputLayout.error = null }
            email.addTextChangedListener { emailInputLayout.error = null }
            password.addTextChangedListener {
                passwordInputLayout.error = null
                passwordConfirmationInputLayout.error = null
            }
            passwordConfirmation.addTextChangedListener {
                passwordInputLayout.error = null
                passwordConfirmationInputLayout.error = null
            }
            passwordConfirmation.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE && v.text.isNotBlank()) {
                    handleSubmitClick()
                }
                false
            }
            submitAction.setOnClickListener {
                handleSubmitClick()
            }
        }
    }

    private fun navigateUp() {
        findNavController().navigateUp()
    }

    private fun handleSubmitClick() {
        with(viewBinding) {
            if (password.sText == passwordConfirmation.sText) {
                submitAction.disable()
                viewModel.signUp(
                    name = name.sText ?: "",
                    email = email.sText ?: "",
                    password = password.sText ?: ""
                )
            } else {
                passwordInputLayout.error = getString(
                    R.string.sign_up_passwords_must_match_error_prompt
                )
                passwordConfirmationInputLayout.error = getString(
                    R.string.sign_up_passwords_must_match_error_prompt
                )
            }
        }
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            handleState(state)
        }
    }

    private fun handleState(state: SignUpState) {
        when (state) {
            SignUpState.Loading -> viewBinding.progressBarHolder.visible()
            is SignUpState.UserSuccessfulSignedUp -> handleUserSuccessfulSignedIn(state)
            is SignUpState.UserNotSignedUp -> handleUserNotSignedIn(state)
        }
    }

    private fun handleUserSuccessfulSignedIn(state: SignUpState.UserSuccessfulSignedUp) {
        viewBinding.progressBarHolder.gone()
        val action = SignUpFragmentDirections.actionSignUpFragmentToFeedFragment(state.user)
        findNavController().navigate(action)
    }

    private fun handleUserNotSignedIn(state: SignUpState.UserNotSignedUp) {
        viewBinding.progressBarHolder.gone()
        viewBinding.submitAction.enable()
        if (state.invalidName) {
            viewBinding.nameInputLayout.error =
                getString(R.string.sign_up_invalid_name_error_prompt)
        }
        if (state.invalidEmail) {
            viewBinding.emailInputLayout.error =
                getString(R.string.sign_up_invalid_email_error_prompt)
        }
        if (state.duplicatedEmail) {
            viewBinding.emailInputLayout.error =
                getString(R.string.sign_up_duplicated_email_error_prompt)
        }
        if (state.invalidPassword) {
            viewBinding.passwordInputLayout.error =
                getString(R.string.sign_up_invalid_password_error_prompt)
        }
    }

}