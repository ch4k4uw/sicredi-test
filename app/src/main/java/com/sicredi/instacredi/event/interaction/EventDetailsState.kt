package com.sicredi.instacredi.event.interaction

import android.content.Intent
import com.sicredi.instacredi.common.interaction.EventDetailsView

sealed class EventDetailsState {
    object Loading : EventDetailsState()
    object SuccessfulCheckedIn : EventDetailsState()
    data class NotCheckedIn(val isMissingConnectivity: Boolean) : EventDetailsState()
    data class DisplayDetails(val details: EventDetailsView) : EventDetailsState()
    data class ShowGoogleMaps(val action: Intent) : EventDetailsState()
    data class ShareEvent(val action: Intent) : EventDetailsState()
    data class EventNotShared(val isMissingConnectivity: Boolean) : EventDetailsState()
}