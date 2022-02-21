package com.sicredi.instacredi.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sicredi.core.extensions.dismissAppWarningFragment
import com.sicredi.core.extensions.showAppWarningFragment
import com.sicredi.core.ui.component.AppWarningFragment
import com.sicredi.core.ui.component.hasAppWarningPrimaryAction
import com.sicredi.instacredi.R
import com.sicredi.instacredi.common.extensions.gone
import com.sicredi.instacredi.common.extensions.showProfileBottomSheetFragment
import com.sicredi.instacredi.common.extensions.visible
import com.sicredi.instacredi.databinding.FragmentFeedBinding
import com.sicredi.instacredi.feed.adapter.EventRecyclerView
import com.sicredi.instacredi.feed.interaction.FeedState
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FeedFragment : Fragment() {
    private val viewModel by viewModels<FeedViewModel>()
    private lateinit var viewBinding: FragmentFeedBinding
    private val navArgs by navArgs<FeedFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFeedBinding.inflate(inflater, container, false).let {
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
            profileAction.setOnClickListener {
                handleProfileClick()
            }
            childFragmentManager.setFragmentResultListener(
                FeedConstants.RequestKey.FeedGenericError,
                viewLifecycleOwner
            ) { _, bundle ->
                if (bundle.hasAppWarningPrimaryAction) {
                    viewModel.loadFeed()
                }
                dismissAppWarningFragment()
            }
            childFragmentManager.setFragmentResultListener(
                FeedConstants.RequestKey.FeedConnectivityError,
                viewLifecycleOwner
            ) { _, bundle ->
                if (bundle.hasAppWarningPrimaryAction) {
                    viewModel.loadFeed()
                }
                dismissAppWarningFragment()
            }
        }
    }

    private fun navigateUp() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun handleProfileClick() {
        showProfileBottomSheetFragment(
            name = navArgs.loggedUser.name, email = navArgs.loggedUser.email
        )
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { status ->
            handleStatus(status)
        }
    }

    private fun handleStatus(state: FeedState) {
        when (state) {
            FeedState.Loading -> viewBinding.progressBarHolder.visible()
            FeedState.SuccessfulLoggedOut -> navigateToSignIn()
            is FeedState.FeedNotLoaded -> handleFeedNotLoaded(state)
            is FeedState.FeedSuccessfulLoaded -> handleSuccessfulLoaded(state)
            else -> Unit
        }
    }

    private fun navigateToSignIn() {
        viewBinding.progressBarHolder.gone()
        val action = FeedFragmentDirections.actionFeedFragmentToSignInFragment()
        findNavController().navigate(action)
    }

    private fun handleFeedNotLoaded(state: FeedState.FeedNotLoaded) {
        viewBinding.progressBarHolder.gone()
        val isGeneric = !state.isMissingConnectivity
        val requestKey = if (isGeneric) {
            FeedConstants.RequestKey.FeedGenericError
        } else {
            FeedConstants.RequestKey.FeedConnectivityError
        }
        val title = if (isGeneric) {
            R.string.app_generic_error_title
        } else {
            R.string.app_no_connectivity_generic_error_title
        }
        val description = if (isGeneric) {
            R.string.app_generic_error_description
        } else {
            R.string.app_no_connectivity_generic_error_description
        }
        val primaryLabel = if (isGeneric) {
            R.string.app_generic_error_positive_action
        } else {
            R.string.app_no_connectivity_generic_error_positive_action
        }
        val secondaryLabel = if (isGeneric) {
            R.string.app_generic_error_negative_action
        } else {
            R.string.app_no_connectivity_generic_error_negative_action
        }
        val icon = if (isGeneric) {
            android.R.drawable.ic_menu_close_clear_cancel
        } else {
            android.R.drawable.stat_sys_warning
        }
        val color = if (isGeneric) {
            AppWarningFragment.BarColor.RED
        } else {
            AppWarningFragment.BarColor.YELLOW
        }
        showAppWarningFragment(requestKey = requestKey) {
            title(getString(title))
            barColor(color)
            icon(icon)
            description(getString(description))
            primaryButtonText(getString(primaryLabel))
            secondaryButtonText(getString(secondaryLabel))
        }
    }

    private fun handleSuccessfulLoaded(state: FeedState.FeedSuccessfulLoaded) {
        viewBinding.progressBarHolder.gone()
        viewBinding.eventList.adapter = EventRecyclerView(events = state.eventHeads) { event ->
            Timber.i(event.toString())
        }
    }
}
