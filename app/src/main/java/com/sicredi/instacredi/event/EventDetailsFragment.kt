package com.sicredi.instacredi.event

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sicredi.core.extensions.dismissAppWarningFragment
import com.sicredi.core.extensions.showAppWarningFragment
import com.sicredi.core.ui.component.AppWarningFragment
import com.sicredi.core.ui.component.hasAppWarningPrimaryAction
import com.sicredi.instacredi.R
import com.sicredi.instacredi.common.extensions.gone
import com.sicredi.instacredi.common.extensions.showError
import com.sicredi.instacredi.common.extensions.showProfileBottomSheetFragment
import com.sicredi.instacredi.common.extensions.visible
import com.sicredi.instacredi.databinding.FragmentEventDetailsBinding
import com.sicredi.instacredi.event.interaction.EventDetailsErrorState
import com.sicredi.instacredi.event.interaction.EventDetailsState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventDetailsFragment : Fragment() {
    private val viewModel by viewModels<EventDetailsViewModel>()
    private lateinit var viewBinding: FragmentEventDetailsBinding
    private val navArgs by navArgs<EventDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentEventDetailsBinding.inflate(inflater, container, false).let {
        it.root.apply { viewBinding = it }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListeners()
        setupObservers()
    }

    private fun setupView() {
        with(viewBinding) {
            toolbar.title = navArgs.eventDetails.title
            appImageDownload.loadImage(Uri.parse(navArgs.eventDetails.image))
            title.text = navArgs.eventDetails.title
            description.text = navArgs.eventDetails.description
        }
    }

    private fun setupListeners() {
        with(viewBinding) {
            toolbar.setNavigationOnClickListener { navigateUp() }
            shareAction.setOnClickListener { handleShareActionClick() }
            profileAction.setOnClickListener { handleProfileActionClick() }
            menuAction.setOnClickListener { handleMenuActionClick() }
        }
        val resultListener = FragmentResultListener { requestKey, bundle ->
            if (bundle.hasAppWarningPrimaryAction) {
                if (requestKey.isCheckIn) {
                    if (bundle.hasAppWarningPrimaryAction) viewModel.performCheckIn()
                } else if (requestKey.isEventSharing) {
                    viewModel.shareEvent()
                }
            }
            dismissAppWarningFragment()
        }
        childFragmentManager.setFragmentResultListener(
            EventDetailsConstants.RequestKey.CheckInGenericError,
            viewLifecycleOwner,
            resultListener
        )
        childFragmentManager.setFragmentResultListener(
            EventDetailsConstants.RequestKey.CheckInConnectivityError,
            viewLifecycleOwner,
            resultListener
        )
        childFragmentManager.setFragmentResultListener(
            EventDetailsConstants.RequestKey.EventSharingGenericError,
            viewLifecycleOwner,
            resultListener
        )
        childFragmentManager.setFragmentResultListener(
            EventDetailsConstants.RequestKey.EventSharingConnectivityError,
            viewLifecycleOwner,
            resultListener
        )
        childFragmentManager.setFragmentResultListener(
            AppWarningFragment.DEFAULT_REQUEST_KEY,
            viewLifecycleOwner,
            resultListener
        )
    }

    private val String.isCheckIn: Boolean
        get() = this == EventDetailsConstants.RequestKey.CheckInGenericError ||
                this == EventDetailsConstants.RequestKey.CheckInConnectivityError

    private val String.isEventSharing: Boolean
        get() = this == EventDetailsConstants.RequestKey.EventSharingGenericError ||
                this == EventDetailsConstants.RequestKey.EventSharingConnectivityError

    private fun navigateUp() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun handleShareActionClick() {
        viewModel.shareEvent()
    }

    private fun handleProfileActionClick() {
        showProfileBottomSheetFragment(
            name = navArgs.loggedUser.name, email = navArgs.loggedUser.email
        )
    }

    private fun handleMenuActionClick() {
        childFragmentManager.showEventOptionsBottomSheetFragment()
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            handleState(state)
        }
    }

    private fun handleState(state: EventDetailsState) {
        when (state) {
            EventDetailsState.Loading -> viewBinding.progressBarHolder.visible()
            is EventDetailsState.NotCheckedIn -> handleEventDetailsError(state = state)
            is EventDetailsState.EventNotShared -> handleEventDetailsError(state = state)
            EventDetailsState.SuccessfulLoggedOut -> handleSuccessfulLoggedOut()
            is EventDetailsState.ShowGoogleMaps -> handleShowGoogleMaps(state)
            is EventDetailsState.ShareEvent -> handleShareEvent(state)
            is EventDetailsState.SuccessfulCheckedIn -> handleSuccessfulCheckedIn()
            else -> Unit
        }
    }

    private fun handleEventDetailsError(state: EventDetailsErrorState) {
        viewBinding.progressBarHolder.gone()
        val isGeneric = !state.isMissingConnectivity
        val requestKey = if (isGeneric) {
            if (state is EventDetailsState.NotCheckedIn) {
                EventDetailsConstants.RequestKey.CheckInGenericError
            } else {
                EventDetailsConstants.RequestKey.EventSharingGenericError
            }
        } else {
            if (state is EventDetailsState.NotCheckedIn) {
                EventDetailsConstants.RequestKey.CheckInConnectivityError
            } else {
                EventDetailsConstants.RequestKey.EventSharingConnectivityError
            }
        }
        showError(isGenericError = !state.isMissingConnectivity, requestKey = requestKey)
    }

    private fun handleSuccessfulLoggedOut() {
        val action = EventDetailsFragmentDirections.actionEventDetailsFragmentToSignInFragment()
        findNavController().navigate(action)
    }

    private fun handleShowGoogleMaps(state: EventDetailsState.ShowGoogleMaps) {
        viewBinding.progressBarHolder.gone()
        startActivity(state.action)
    }

    private fun handleShareEvent(state: EventDetailsState.ShareEvent) {
        viewBinding.progressBarHolder.gone()
        startActivity(state.action)
    }

    private fun handleSuccessfulCheckedIn() {
        viewBinding.progressBarHolder.gone()
        showAppWarningFragment {
            title(
                getString(R.string.event_details_successful_checked_in_message_title)
            )
            description(
                getString(R.string.event_details_successful_checked_in_message_description)
            )
            icon(android.R.drawable.ic_dialog_info)
            barColor(AppWarningFragment.BarColor.GREEN)
            primaryButtonText(
                getString(R.string.event_details_successful_checked_in_message_primary_button)
            )
        }
    }

}