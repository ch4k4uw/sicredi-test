package com.sicredi.instacredi

import com.sicredi.instacredi.event.EventDetailsConstants
import com.sicredi.instacredi.feed.FeedConstants
import com.sicredi.presenter.common.extensions.marshall
import com.sicredi.presenter.common.interaction.EventDetailsView
import com.sicredi.presenter.common.interaction.UserView

object MainNavigationConstants {
    object Nav {
        object Feed {
            const val StartingRoute = "feed"
            const val Route = "$StartingRoute?" +
                    "${FeedConstants.Key.LoggedUser}={${FeedConstants.Key.LoggedUser}}"

            fun build(user: UserView): String =
                "$StartingRoute?${FeedConstants.Key.LoggedUser}=${user.marshall()}"
        }

        object EventDetails {
            private const val loggedUserArg = EventDetailsConstants.Key.LoggedUser
            private const val detailsArg = EventDetailsConstants.Key.Details
            const val StartingRoute = "event/details"
            const val Route = "$StartingRoute?" +
                    "$loggedUserArg={$loggedUserArg}&$detailsArg={$detailsArg}"

            fun build(user: UserView, details: EventDetailsView): String =
                "$StartingRoute?" +
                        "$loggedUserArg={${user.marshall()}}&$detailsArg={${details.marshall()}}"
        }

        object SignIn {
            const val StartingRoute = "sign/in"
            const val Route = StartingRoute
        }

        object SignUp {
            const val StartingRoute = "sign/up"
            const val Route = StartingRoute
        }
    }
}