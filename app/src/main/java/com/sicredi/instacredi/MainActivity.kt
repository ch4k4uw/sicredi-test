package com.sicredi.instacredi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import com.sicredi.instacredi.common.extensions.navHostFragment
import com.sicredi.instacredi.common.interaction.EventDetailsView
import com.sicredi.instacredi.common.interaction.UserView
import com.sicredi.instacredi.databinding.ActivityMainBinding
import com.sicredi.instacredi.event.EventDetailsConstants
import com.sicredi.instacredi.feed.FeedConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    private val loggedUser: UserView?
        get() = intent.extras?.getParcelable(MainActivityConstants.Key.LoggedUser)

    private val eventDetails: EventDetailsView?
        get() = intent.extras?.getParcelable(MainActivityConstants.Key.EventDetails)

    private val destinationId: Int
        get() = intent.extras?.getInt(
            MainActivityConstants.Key.DestinationId, R.id.signInFragment
        ) ?: R.id.signInFragment

    private val navHostFragment by navHostFragment(id = R.id.mainNavHostFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        val eventDetails = eventDetails
        val loggedUser = loggedUser
        val navDestination = object {
            val id = destinationId
            val args = when {
                eventDetails != null -> NavArgument.Builder()
                    .setDefaultValue(eventDetails)
                    .setType(NavType.ParcelableType(EventDetailsView::class.java))
                    .build()
                loggedUser != null -> NavArgument.Builder()
                    .setDefaultValue(loggedUser)
                    .setType(NavType.ParcelableType(UserView::class.java))
                    .build()
                else -> null
            }
        }
        navHostFragment.navController.apply {
            graph = navInflater.inflate(R.navigation.nav_graph).apply {
                startDestination = navDestination.id
                if (navDestination.args != null) {
                    if (loggedUser != null) {
                        addArgument(
                            FeedConstants.Key.LoggedUser, navDestination.args
                        )
                    } else {
                        addArgument(
                            EventDetailsConstants.Key.Details, navDestination.args
                        )
                    }
                }
            }
        }
    }
}

fun AppCompatActivity.startMainActivityForFeedFragment(user: UserView) {
    startActivity(
        Intent(this, MainActivity::class.java).apply {
            putExtra(MainActivityConstants.Key.DestinationId, R.id.feedFragment)
            putExtra(MainActivityConstants.Key.LoggedUser, user)
        }
    )
}

fun AppCompatActivity.startMainActivityForSignInFragment() {
    startActivity(Intent(this, MainActivity::class.java))
}

fun AppCompatActivity.startMainActivityForEventDetails(eventDetails: EventDetailsView) {
    startActivity(
        Intent(this, MainActivity::class.java).apply {
            putExtra(MainActivityConstants.Key.DestinationId, R.id.eventDetailsFragment)
            putExtra(MainActivityConstants.Key.EventDetails, eventDetails)
        }
    )
}