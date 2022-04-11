package com.sicredi.presenter.event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sicredi.core.network.domain.data.NoConnectivityException
import com.sicredi.presenter.common.BaseViewModel
import com.sicredi.presenter.common.interaction.EventDetailsView
import com.sicredi.presenter.common.interaction.mapsIntent
import com.sicredi.presenter.common.uc.PerformLogout
import com.sicredi.presenter.common.uc.ShareEvent
import com.sicredi.presenter.event.interaction.EventDetailsState
import com.sicredi.presenter.event.uc.PerformCheckIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val performCheckIn: PerformCheckIn,
    private val shareEvent: ShareEvent,
    private val performLogout: PerformLogout,
) : BaseViewModel<EventDetailsState>() {
    init {
        viewModelScope.launch {
            state emit EventDetailsState.DisplayDetails(
                details = details
            )
        }
    }

    private val details: EventDetailsView
        get() = savedStateHandle.get(EventDetailsConstants.Key.Details) ?: EventDetailsView.Empty

    fun performCheckIn() {
        viewModelScope.launch {
            state emit EventDetailsState.Loading
            performCheckIn(eventId = details.id)
                .catch { cause ->
                    Timber.e(cause)
                    state emit EventDetailsState.NotCheckedIn(
                        isMissingConnectivity = cause is NoConnectivityException
                    )
                }
                .collect {
                    state emit EventDetailsState.SuccessfulCheckedIn
                }
        }
    }

    fun showGoogleMaps() {
        viewModelScope.launch {
            state emit EventDetailsState.ShowGoogleMaps(action = details.mapsIntent)
        }
    }

    fun shareEvent() {
        viewModelScope.launch {
            state emit EventDetailsState.Loading
            shareEvent(eventId = details.id)
                .catch { cause ->
                    Timber.e(cause)
                    state emit EventDetailsState.EventNotShared(
                        isMissingConnectivity = cause is NoConnectivityException
                    )
                }
                .collect {
                    state emit EventDetailsState.ShareEvent(action = it)
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            state emit EventDetailsState.Loading
            performLogout()
                .first()
            state emit EventDetailsState.SuccessfulLoggedOut
        }
    }

}