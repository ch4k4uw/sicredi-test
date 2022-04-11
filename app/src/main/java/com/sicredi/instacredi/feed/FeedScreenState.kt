package com.sicredi.instacredi.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.os.bundleOf
import com.sicredi.presenter.common.interaction.EventDetailsView
import com.sicredi.presenter.feed.interaction.EventHeadView
import com.sicredi.presenter.feed.interaction.FeedState

class FeedScreenState(
    private val onShowEventDetails: (EventDetailsView) -> Unit,
    private val onLoggedOut: () -> Unit
) {
    var showLoading by mutableStateOf(false)
    val events = mutableStateListOf<EventHeadView>()
    var showEventsLoadingGenericError by mutableStateOf(false)
    var showEventsLoadingConnectionError by mutableStateOf(false)
    var showEventDetailsLoadingGenericError by mutableStateOf(false)
    var showEventDetailsLoadingConnectionError by mutableStateOf(false)
    var showProfileDialog by mutableStateOf(false)

    fun handleState(state: FeedState) {
        when (state) {
            FeedState.Loading -> showLoading = true
            is FeedState.FeedSuccessfulLoaded -> {
                showLoading = false
                events.clear()
                events.addAll(state.eventHeads)
            }
            is FeedState.EventDetailsSuccessfulLoaded -> {
                showLoading = false
                onShowEventDetails(state.details)
            }
            FeedState.SuccessfulLoggedOut -> { showLoading = false; onLoggedOut() }
            is FeedState.FeedNotLoaded -> {
                if (state.isMissingConnectivity) showEventsLoadingConnectionError = true
                else showEventsLoadingGenericError = true
            }
            is FeedState.EventDetailsNotLoaded -> {
                if (state.isMissingConnectivity) showEventDetailsLoadingConnectionError = true
                else showEventDetailsLoadingGenericError = true
            }
            else -> Unit
        }
    }

    companion object {
        fun saver(
            onShowEventDetails: (EventDetailsView) -> Unit,
            onLoggedOut: () -> Unit
        ): Saver<FeedScreenState, *> = Saver(
            save = {
                bundleOf(
                    "ldn" to it.showLoading,
                    "list" to it.events.toTypedArray(),
                    "errEvts1" to it.showEventsLoadingGenericError,
                    "errEvts2" to it.showEventsLoadingConnectionError,
                    "errEvtD1" to it.showEventDetailsLoadingGenericError,
                    "errEvtD2" to it.showEventDetailsLoadingConnectionError,
                    "shwPflDlg" to it.showProfileDialog,
                )
            },
            restore = {
                FeedScreenState(
                    onShowEventDetails = onShowEventDetails, onLoggedOut = onLoggedOut
                ).apply {
                    showLoading = it.getBoolean("ldn")
                    events.addAll(
                        it
                            .getParcelableArray("list")
                            ?.map { item -> item as EventHeadView }
                            ?: listOf()
                    )
                    showEventsLoadingGenericError = it.getBoolean("errEvts1")
                    showEventsLoadingConnectionError = it.getBoolean("errEvts2")
                    showEventDetailsLoadingGenericError = it.getBoolean("errEvtD1")
                    showEventDetailsLoadingConnectionError = it.getBoolean("errEvtD2")
                    showProfileDialog = it.getBoolean("shwPflDlg")
                }
            }
        )
    }
}

@Composable
fun rememberFeedScreenState(
    onShowEventDetails: (EventDetailsView) -> Unit, onLoggedOut: () -> Unit
) = rememberSaveable(
    saver = FeedScreenState.saver(
        onShowEventDetails = onShowEventDetails,
        onLoggedOut = onLoggedOut
    )
) { FeedScreenState(onShowEventDetails = onShowEventDetails, onLoggedOut = onLoggedOut) }