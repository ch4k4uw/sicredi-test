package com.sicredi.presenter.feed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sicredi.core.network.domain.data.NoConnectivityException
import com.sicredi.presenter.common.BaseViewModel
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val findAllEvents: FindAllEvents,
    private val findEventDetails: FindEventDetails,
    private val performLogout: PerformLogout
) : BaseViewModel<FeedState>() {
    private var eventViews: List<EventHeadView>?
        get() = savedStateHandle.get<Array<EventHeadView>>("events")?.toList()
        set(events) {
            if (events != null) {
                savedStateHandle.set("events", events.toTypedArray())
            }
        }

    fun loadFeed() {
        viewModelScope.launch {
            state emit FeedState.Loading
            if (eventViews == null) {
                findAllEvents()
                    .catch { cause ->
                        Timber.e(cause)
                        state emit FeedState
                            .FeedNotLoaded(isMissingConnectivity = cause is NoConnectivityException)
                    }
                    .collect { events ->
                        state emit FeedState
                            .FeedSuccessfulLoaded(
                                eventHeads = events.asEventHeadViews.apply { eventViews = this }
                            )
                    }
            } else {
                state emit FeedState
                    .FeedSuccessfulLoaded(
                        eventHeads = eventViews!!
                    )
            }
        }
    }

    fun findDetails(id: String) {
        viewModelScope.launch {
            state emit FeedState.Loading
            findEventDetails(id = id)
                .catch { cause ->
                    Timber.e(cause)
                    state emit FeedState.EventDetailsNotLoaded(
                        isMissingConnectivity = cause is NoConnectivityException,
                        id = id
                    )
                }
                .collect { event ->
                    state emit FeedState
                        .EventDetailsSuccessfulLoaded(details = event.asEventDetailView)
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            state emit FeedState.Loading
            performLogout()
                .first()
            state emit FeedState.SuccessfulLoggedOut
        }
    }
}