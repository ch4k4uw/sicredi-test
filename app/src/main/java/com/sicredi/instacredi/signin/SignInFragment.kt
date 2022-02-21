package com.sicredi.instacredi.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sicredi.core.extensions.dismissAppWarningFragment
import com.sicredi.core.extensions.showAppWarningFragment
import com.sicredi.core.ui.component.AppWarningFragment
import com.sicredi.instacredi.R
import com.sicredi.instacredi.common.extensions.dataStore
import com.sicredi.instacredi.common.extensions.gone
import com.sicredi.instacredi.common.extensions.requestSelection
import com.sicredi.instacredi.common.extensions.restoreLastLogin
import com.sicredi.instacredi.common.extensions.sText
import com.sicredi.instacredi.common.extensions.visible
import com.sicredi.instacredi.databinding.FragmentSignInBinding
import com.sicredi.instacredi.signin.interaction.SignInState
import com.sicredi.instacredi.signin.interaction.SignInUserState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private val viewModel by viewModels<SignInViewModel>()
    private lateinit var viewBinding: FragmentSignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            viewBinding.email.sText = withContext(Dispatchers.IO) {
                dataStore.restoreLastLogin()
            }
        }
    }

    private fun handleUserNotSignedIn() {
        viewBinding.progressBarHolder.gone()
        showAppWarningFragment(
            requestKey = SignInConstants.RequestKey.SignInError
        ) {
            title(getString(R.string.sign_in_invalid_user_or_pass_error_title))
            barColor(AppWarningFragment.BarColor.RED)
            description(
                getString(R.string.sign_in_invalid_user_or_pass_error_description)
            )
            primaryButtonText(
                getString(R.string.sign_in_invalid_user_or_pass_error_positive_action)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSignInBinding.inflate(inflater, container, false).let {
        it.root.apply { viewBinding = it }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                SignInState.Loading -> viewBinding.progressBarHolder.visible()
                SignInState.Loaded -> viewBinding.progressBarHolder.gone()
                is SignInUserState -> {
                    handleLoggedUser(state)
                }
                SignInState.UserNotSignedIn -> {
                    handleUserNotSignedIn()
                }
            }
        }
    }

    private fun handleLoggedUser(state: SignInUserState) {
        viewBinding.progressBarHolder.gone()
        storeLastLogin()
        val action = SignInFragmentDirections
            .actionSignInFragmentToFeedFragment(state.user)
        findNavController().navigate(action)
    }

    private fun storeLastLogin() {
        lifecycleScope.launch(Dispatchers.IO) {
            dataStore.restoreLastLogin()
        }
    }

    private fun setupListeners() {
        with(viewBinding) {
            password.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE && v.text.isNotBlank()) {
                    performSignIn()
                    //return@setOnEditorActionListener true
                }
                false
            }
            signInAction.setOnClickListener {
                performSignIn()
            }
            signUpAction.setOnClickListener {
                navigateToSignUp()
            }
        }
        childFragmentManager.setFragmentResultListener(
            SignInConstants.RequestKey.SignInError, this
        ) { _, _ ->
            dismissAppWarningFragment()
            viewBinding.password.requestSelection()
        }
    }

    private fun performSignIn() {
        with(viewBinding) {
            viewModel.signIn(email = email.sText ?: "", password = password.sText ?: "")
        }
    }

    private fun navigateToSignUp() {
        val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        findNavController().navigate(action)
    }
}