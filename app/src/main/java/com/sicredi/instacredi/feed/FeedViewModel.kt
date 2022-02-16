package com.sicredi.instacredi.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sicredi.core.data.LiveEvent
import com.sicredi.core.network.domain.data.NoConnectivityException
import com.sicredi.instacredi.feed.interaction.FeedState
import com.sicredi.instacredi.feed.interaction.asEventDetailView
import com.sicredi.instacredi.feed.interaction.asEventHeadViews
import com.sicredi.instacredi.feed.uc.FindAllEvents
import com.sicredi.instacredi.feed.uc.FindEventDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val findAllEvents: FindAllEvents,
    private val findEventDetails: FindEventDetails,
) : ViewModel() {
    private val mutableState = LiveEvent<FeedState>()
    val state: LiveData<FeedState> = mutableState

    fun loadFeed() {
        mutableState.value = FeedState.Loading
        viewModelScope.launch {
            findAllEvents()
                .catch { cause ->
                    Timber.e(cause)
                    mutableState.value = FeedState
                        .FeedNotLoaded(isMissingConnectivity = cause is NoConnectivityException)
                }
                .collect { events ->
                    mutableState.value = FeedState
                        .FeedSuccessfulLoaded(eventHeads = events.asEventHeadViews )
                }
        }
    }

    fun findDetail(id: String) {
        mutableState.value = FeedState.Loading
        viewModelScope.launch {
            findEventDetails(id = id)
                .catch { cause ->
                    Timber.e(cause)
                    mutableState.value = FeedState.EventDetailsNotLoaded(
                        isMissingConnectivity = cause is NoConnectivityException
                    )
                }
                .collect { event ->
                    mutableState.value = FeedState
                        .EventDetailsSuccessfulLoaded(details = event.asEventDetailView)
                }
        }
    }
}