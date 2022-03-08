package com.sicredi.presenter.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sicredi.core.data.LiveEvent
import com.sicredi.core.network.domain.data.NoConnectivityException
import com.sicredi.presenter.common.interaction.asEventDetailView
import com.sicredi.presenter.common.uc.FindEventDetails
import com.sicredi.presenter.common.uc.PerformLogout
import com.sicredi.presenter.feed.interaction.EventHeadView
import com.sicredi.presenter.feed.interaction.FeedState
import com.sicredi.presenter.feed.interaction.asEventHeadViews
import com.sicredi.presenter.feed.uc.FindAllEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val findAllEvents: FindAllEvents,
    private val findEventDetails: FindEventDetails,
    private val performLogout: PerformLogout
) : ViewModel() {
    private val mutableState = LiveEvent<FeedState>()
    val state: LiveData<FeedState> = mutableState

    private var eventViews: List<EventHeadView>?
        get() = savedStateHandle.get<Array<EventHeadView>>("events")?.toList()
        set(events) {
            if (events != null) {
                savedStateHandle.set("events", events.toTypedArray())
            }
        }

    fun loadFeed() {
        mutableState.value = FeedState.Loading
        viewModelScope.launch {
            if (eventViews == null) {
                findAllEvents()
                    .catch { cause ->
                        Timber.e(cause)
                        mutableState.value = FeedState
                            .FeedNotLoaded(isMissingConnectivity = cause is NoConnectivityException)
                    }
                    .collect { events ->
                        mutableState.value = FeedState
                            .FeedSuccessfulLoaded(
                                eventHeads = events.asEventHeadViews.apply { eventViews = this }
                            )
                    }
            } else {
                mutableState.value = FeedState
                    .FeedSuccessfulLoaded(
                        eventHeads = eventViews!!
                    )
            }
        }
    }

    fun findDetails(id: String) {
        mutableState.value = FeedState.Loading
        viewModelScope.launch {
            findEventDetails(id = id)
                .catch { cause ->
                    Timber.e(cause)
                    mutableState.value = FeedState.EventDetailsNotLoaded(
                        isMissingConnectivity = cause is NoConnectivityException,
                        id = id
                    )
                }
                .collect { event ->
                    mutableState.value = FeedState
                        .EventDetailsSuccessfulLoaded(details = event.asEventDetailView)
                }
        }
    }

    fun logout() {
        mutableState.value = FeedState.Loading
        viewModelScope.launch {
            performLogout()
                .first()
            mutableState.value = FeedState.SuccessfulLoggedOut
        }
    }
}