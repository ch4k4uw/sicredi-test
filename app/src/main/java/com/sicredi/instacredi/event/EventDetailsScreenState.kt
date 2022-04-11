package com.sicredi.instacredi.event

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.sicredi.presenter.common.interaction.EventDetailsView
import com.sicredi.presenter.event.interaction.EventDetailsErrorState
import com.sicredi.presenter.event.interaction.EventDetailsState
import timber.log.Timber

class EventDetailsScreenState(
    private val onShowGoogleMaps: (Intent) -> Unit,
    private val onShareEvent: (Intent) -> Unit,
    private val onLoggedOut: () -> Unit,
) {
    var showLoading by mutableStateOf(false)
    var details by mutableStateOf(EventDetailsView.Empty)
        private set
    var showCheckedInSuccess by mutableStateOf(false)
    var showProfileDialog by mutableStateOf(false)
    var showEventOptionsDialog by mutableStateOf(false)
    var showCheckInGenericError by mutableStateOf(false)
    var showCheckInConnectionError by mutableStateOf(false)
    var showEventSharingGenericError by mutableStateOf(false)
    var showEventSharingConnectionError by mutableStateOf(false)

    fun handleState(state: EventDetailsState) {
        when (state) {
            EventDetailsState.Loading -> showLoading = true
            is EventDetailsState.NotCheckedIn -> handleEventDetailsError(state = state)
            is EventDetailsState.EventNotShared -> handleEventDetailsError(state = state)
            EventDetailsState.SuccessfulLoggedOut -> handleSuccessfulLoggedOut()
            is EventDetailsState.ShowGoogleMaps -> handleShowGoogleMaps(state = state)
            is EventDetailsState.ShareEvent -> handleShareEvent(state = state)
            is EventDetailsState.SuccessfulCheckedIn -> handleSuccessfulCheckedIn()
            is EventDetailsState.DisplayDetails -> handleDisplayDetails(state = state)
        }
    }

    private fun handleEventDetailsError(state: EventDetailsErrorState) {
        showLoading = false
        if (state is EventDetailsState.NotCheckedIn) {
            showCheckInGenericError = !state.isMissingConnectivity
            showCheckInConnectionError = state.isMissingConnectivity
        } else {
            showEventSharingGenericError = !state.isMissingConnectivity
            showEventSharingConnectionError = state.isMissingConnectivity
        }
    }

    private fun handleSuccessfulLoggedOut() {
        showLoading = false
        onLoggedOut()
    }

    private fun handleShowGoogleMaps(state: EventDetailsState.ShowGoogleMaps) {
        showLoading = false
        onShowGoogleMaps(state.action)
    }

    private fun handleShareEvent(state: EventDetailsState.ShareEvent) {
        showLoading = false
        onShareEvent(state.action)
    }

    private fun handleSuccessfulCheckedIn() {
        showLoading = false
        showCheckedInSuccess = true
    }

    private fun handleDisplayDetails(state: EventDetailsState.DisplayDetails) {
        showLoading = false
        details = state.details
    }

    companion object {
        fun saver(
            onShowGoogleMaps: (Intent) -> Unit,
            onShareEvent: (Intent) -> Unit,
            onLoggedOut: () -> Unit,
        ): Saver<EventDetailsScreenState, *> = Saver(
            save = {
                Timber.d("Saving event details screen state.")
                arrayListOf(
                    it.showLoading,
                    it.details,
                    it.showProfileDialog,
                    it.showEventOptionsDialog,
                    it.showCheckInGenericError,
                    it.showCheckInConnectionError,
                    it.showEventSharingGenericError,
                    it.showEventSharingConnectionError,
                )
            },
            restore = {
                Timber.d("Restoring event details screen state.")
                EventDetailsScreenState(
                    onShowGoogleMaps = onShowGoogleMaps,
                    onShareEvent = onShareEvent,
                    onLoggedOut = onLoggedOut
                ).apply {
                    showLoading = it[0] as Boolean
                    details = it[1] as EventDetailsView
                    showProfileDialog = it[2] as Boolean
                    showEventOptionsDialog = it[3] as Boolean
                    showCheckInGenericError = it[4] as Boolean
                    showCheckInConnectionError = it[5] as Boolean
                    showEventSharingGenericError = it[6] as Boolean
                    showEventSharingConnectionError = it[7] as Boolean
                }
            }
        )
    }
}

@Composable
fun rememberEventDetailsScreenState(
    onShowGoogleMaps: (Intent) -> Unit,
    onShareEvent: (Intent) -> Unit,
    onLoggedOut: () -> Unit,
) = rememberSaveable(
    saver = EventDetailsScreenState.saver(
        onShowGoogleMaps = onShowGoogleMaps,
        onShareEvent = onShareEvent,
        onLoggedOut = onLoggedOut
    )
) {
    EventDetailsScreenState(
        onShowGoogleMaps = onShowGoogleMaps,
        onShareEvent = onShareEvent,
        onLoggedOut = onLoggedOut
    )
}