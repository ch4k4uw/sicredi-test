package com.sicredi.instacredi.event.interaction

import android.content.Intent
import com.sicredi.instacredi.common.interaction.EventDetailsView

sealed interface EventDetailsErrorState {
    val isMissingConnectivity: Boolean
}

sealed class EventDetailsState {
    object Loading : EventDetailsState()
    object SuccessfulCheckedIn : EventDetailsState()
    data class NotCheckedIn(override val isMissingConnectivity: Boolean) : EventDetailsState(),
        EventDetailsErrorState

    data class DisplayDetails(val details: EventDetailsView) : EventDetailsState()
    data class ShowGoogleMaps(val action: Intent) : EventDetailsState()
    data class ShareEvent(val action: Intent) : EventDetailsState()
    data class EventNotShared(override val isMissingConnectivity: Boolean) : EventDetailsState(),
        EventDetailsErrorState

    object SuccessfulLoggedOut : EventDetailsState()
}