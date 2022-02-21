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
            val args = arrayListOf<Pair<String, NavArgument>>().apply {
                if (eventDetails != null) {
                    val data = Pair(
                        EventDetailsConstants.Key.Details,
                        NavArgument.Builder()
                            .setDefaultValue(eventDetails)
                            .setType(NavType.ParcelableType(EventDetailsView::class.java))
                            .build()
                    )
                    add(data)
                }
                if (loggedUser != null) {
                    val key = if (eventDetails == null) {
                        FeedConstants.Key.LoggedUser
                    } else {
                        EventDetailsConstants.Key.LoggedUser
                    }
                    val data = Pair(
                        key,
                        NavArgument.Builder()
                            .setDefaultValue(loggedUser)
                            .setType(NavType.ParcelableType(UserView::class.java))
                            .build()
                    )
                    add(data)
                }
            }
        }
        navHostFragment.navController.apply {
            graph = navInflater.inflate(R.navigation.nav_graph).apply {
                startDestination = navDestination.id
                navDestination.args.forEach { addArgument(it.first, it.second) }
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

fun AppCompatActivity.startMainActivityForEventDetails(
    user: UserView, eventDetails: EventDetailsView
) {
    startActivity(
        Intent(this, MainActivity::class.java).apply {
            putExtra(MainActivityConstants.Key.DestinationId, R.id.eventDetailsFragment)
            putExtra(MainActivityConstants.Key.EventDetails, eventDetails)
            putExtra(MainActivityConstants.Key.LoggedUser, user)
        }
    )
}