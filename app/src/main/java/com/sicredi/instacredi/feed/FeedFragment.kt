package com.sicredi.instacredi.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sicredi.core.extensions.dismissAppWarningFragment
import com.sicredi.core.ui.component.AppWarningFragment
import com.sicredi.core.ui.component.hasAppWarningPrimaryAction
import com.sicredi.core.ui.component.optionParams
import com.sicredi.instacredi.common.extensions.gone
import com.sicredi.instacredi.common.extensions.showError
import com.sicredi.instacredi.common.extensions.showProfileBottomSheetFragment
import com.sicredi.instacredi.common.extensions.visible
import com.sicredi.instacredi.databinding.FragmentFeedBinding
import com.sicredi.instacredi.feed.adapter.EventRecyclerView
import com.sicredi.instacredi.feed.interaction.FeedErrorState
import com.sicredi.instacredi.feed.interaction.FeedState
import dagger.hilt.android.AndroidEntryPoint

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

    override fun onResume() {
        super.onResume()
        viewModel.loadFeed()
    }

    private fun setupListeners() {
        with(viewBinding) {
            toolbar.setNavigationOnClickListener { navigateUp() }
            profileAction.setOnClickListener {
                handleProfileClick()
            }
            val resultListener = FragmentResultListener { requestKey, bundle ->
                if (bundle.hasAppWarningPrimaryAction) {
                    if (requestKey.isFeedListing) {
                        if (bundle.hasAppWarningPrimaryAction) viewModel.loadFeed()
                    } else if (requestKey.isEventDetailing) {
                        val id = bundle.optionParams
                            ?.getString("id")
                        if (id != null) {
                            viewModel.findDetails(id)
                        }
                    }
                }
                dismissAppWarningFragment()
            }
            childFragmentManager.setFragmentResultListener(
                FeedConstants.RequestKey.FeedGenericError,
                viewLifecycleOwner,
                resultListener
            )
            childFragmentManager.setFragmentResultListener(
                FeedConstants.RequestKey.FeedConnectivityError,
                viewLifecycleOwner,
                resultListener
            )
            childFragmentManager.setFragmentResultListener(
                FeedConstants.RequestKey.EventDetailsGenericError,
                viewLifecycleOwner,
                resultListener
            )
            childFragmentManager.setFragmentResultListener(
                FeedConstants.RequestKey.EventDetailsConnectivityError,
                viewLifecycleOwner,
                resultListener
            )
        }
    }

    private val String.isFeedListing: Boolean
        get() = this == FeedConstants.RequestKey.FeedGenericError ||
                this == FeedConstants.RequestKey.FeedConnectivityError

    private val String.isEventDetailing: Boolean
        get() = this == FeedConstants.RequestKey.EventDetailsGenericError ||
                this == FeedConstants.RequestKey.EventDetailsConnectivityError

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
            is FeedState.EventDetailsNotLoaded -> handleFeedNotLoaded(state)
            is FeedState.FeedSuccessfulLoaded -> handleSuccessfulLoaded(state)
            is FeedState.EventDetailsSuccessfulLoaded -> handleEventDetailsSuccessfulLoaded(state)
        }
    }

    private fun navigateToSignIn() {
        viewBinding.progressBarHolder.gone()
        val action = FeedFragmentDirections.actionFeedFragmentToSignInFragment()
        findNavController().navigate(action)
    }

    private fun handleFeedNotLoaded(state: FeedErrorState) {
        viewBinding.progressBarHolder.gone()
        val isGeneric = !state.isMissingConnectivity
        var optionParams: Bundle? = null
        val requestKey = if (isGeneric) {
            if (state is FeedState.EventDetailsNotLoaded) {
                optionParams = bundleOf("id" to state.id)
                FeedConstants.RequestKey.EventDetailsGenericError
            } else {
                FeedConstants.RequestKey.FeedGenericError
            }
        } else {
            if (state is FeedState.EventDetailsNotLoaded) {
                optionParams = bundleOf("id" to state.id)
                FeedConstants.RequestKey.EventDetailsConnectivityError
            } else {
                FeedConstants.RequestKey.FeedConnectivityError
            }
        }
        showError(
            isGenericError = !state.isMissingConnectivity,
            requestKey = requestKey,
            optionParams = optionParams
        )
    }

    private fun handleSuccessfulLoaded(state: FeedState.FeedSuccessfulLoaded) {
        viewBinding.progressBarHolder.gone()
        viewBinding.eventList.adapter = EventRecyclerView(events = state.eventHeads) { event ->
            viewModel.findDetails(event.id)
        }
    }

    private fun handleEventDetailsSuccessfulLoaded(state: FeedState.EventDetailsSuccessfulLoaded) {
        viewBinding.progressBarHolder.gone()
        val action = FeedFragmentDirections.actionFeedFragmentToEventDetailsFragment(
            state.details,
            navArgs.loggedUser
        )
        findNavController().navigate(action)
    }
}
