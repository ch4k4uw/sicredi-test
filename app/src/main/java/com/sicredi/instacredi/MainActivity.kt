package com.sicredi.instacredi

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.sicredi.core.extensions.AppBackground
import com.sicredi.core.ui.compose.AppTheme
import com.sicredi.presenter.common.interaction.EventDetailsView
import com.sicredi.presenter.common.interaction.UserView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                AppBackground {
                    MainNavigation()
                }
            }
        }
    }
}

fun Context.findAppCompatActivity(): AppCompatActivity {
    var context = this
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) return context
        context = context.baseContext
    }
    throw IllegalStateException("It should be called in the context of an Activity")
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