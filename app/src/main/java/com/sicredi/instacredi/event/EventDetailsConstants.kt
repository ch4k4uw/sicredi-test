package com.sicredi.instacredi.event

object EventDetailsConstants {
    object Key {
        const val LoggedUser = "loggedUser"
        const val Details = "eventDetails"
    }
    object RequestKey {
        const val CheckInGenericError = "key.check-in.request.error.generic"
        const val CheckInConnectivityError = "key.check-in.request.error.connectivity"
        const val EventSharingGenericError = "key.event-sharing.request.error.generic"
        const val EventSharingConnectivityError = "key.event-sharing.request.error.connectivity"
    }
}