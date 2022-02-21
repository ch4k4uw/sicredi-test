package com.sicredi.instacredi.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sicredi.core.data.LiveEvent
import com.sicredi.core.network.domain.data.NoConnectivityException
import com.sicredi.instacredi.common.interaction.EventDetailsView
import com.sicredi.instacredi.common.interaction.mapsIntent
import com.sicredi.instacredi.common.uc.PerformLogout
import com.sicredi.instacredi.common.uc.ShareEvent
import com.sicredi.instacredi.event.interaction.EventDetailsState
import com.sicredi.instacredi.event.uc.PerformCheckIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val performCheckIn: PerformCheckIn,
    private val shareEvent: ShareEvent,
    private val performLogout: PerformLogout,
): ViewModel() {
    private val mutableState = LiveEvent<EventDetailsState>()
    val state: LiveData<EventDetailsState> = mutableState

    init {
        viewModelScope.launch {
            while (!mutableState.hasObservers()) yield()
            mutableState.value = EventDetailsState.DisplayDetails(
                details = details
            )
        }
    }

    private val details: EventDetailsView
        get() = savedStateHandle.get(EventDetailsConstants.Key.Details) ?: EventDetailsView.Empty

    fun performCheckIn() {
        viewModelScope.launch {
            mutableState.value = EventDetailsState.Loading
            performCheckIn(eventId = details.id)
                .catch { cause ->
                    Timber.e(cause)
                    mutableState.value = EventDetailsState.NotCheckedIn(
                        isMissingConnectivity = cause is NoConnectivityException
                    )
                }
                .collect {
                    mutableState.value = EventDetailsState.SuccessfulCheckedIn
                }
        }
    }

    fun showGoogleMaps() {
        mutableState.postValue(EventDetailsState.ShowGoogleMaps(action = details.mapsIntent))
    }

    fun shareEvent() {
        viewModelScope.launch {
            mutableState.value = EventDetailsState.Loading
            shareEvent(eventId = details.id)
                .catch { cause ->
                    Timber.e(cause)
                    mutableState.value = EventDetailsState.EventNotShared(
                        isMissingConnectivity = cause is NoConnectivityException
                    )
                }
                .collect {
                    mutableState.value = EventDetailsState.ShareEvent(action = it)
                }
        }
    }

    fun logout() {
        mutableState.value = EventDetailsState.Loading
        viewModelScope.launch {
            performLogout()
                .first()
            mutableState.value = EventDetailsState.SuccessfulLoggedOut
        }
    }

}