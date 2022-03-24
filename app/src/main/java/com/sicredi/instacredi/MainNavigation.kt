package com.sicredi.instacredi

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.sicredi.instacredi.common.extensions.viewModel
import com.sicredi.instacredi.feed.FeedConstants
import com.sicredi.instacredi.feed.FeedScreen
import com.sicredi.presenter.common.interaction.UserView
import com.sicredi.presenter.feed.FeedViewModel
import timber.log.Timber

private object MainNavigationConstants {
    object Navigation {
        const val Feed = "feed"
        const val SignIn = "sign/in"
        const val SignUp = "sign/up"
    }
    object Route {
        const val Feed = "${Navigation.Feed}?" +
                "${FeedConstants.Key.LoggedUser}={${FeedConstants.Key.LoggedUser}}"
        const val SignIn = "sign/in"
        const val SignUp = "sign/up"
    }

    fun idToComposeNavigation(id: Int) = when (id) {
        R.id.signInFragment -> Navigation.SignIn
        R.id.signUpFragment -> Navigation.SignUp
        R.id.feedFragment -> Navigation.Feed
        else -> Navigation.SignIn
    }

}

@Composable
fun MainNavigation() {
    val context = LocalContext.current
    val navController = rememberNavController()
    var startNavigation by rememberSaveable { mutableStateOf("") }

    if (startNavigation.isNotEmpty()) {
        Timber.d(startNavigation)
        NavHost(
            navController = navController,
            startDestination = navController.tryGraph()?.startDestinationRoute ?: startNavigation
        ) {
            composable(
                route = MainNavigationConstants.Navigation.Feed,
                arguments = listOf(
                    navArgument(name = FeedConstants.Key.LoggedUser) {
                        nullable = true
                        defaultValue = null
                    }
                ),
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = MainNavigationConstants.Route.Feed
                    }
                )
            ) { navBackStackEntry ->
                val viewModel: FeedViewModel = navBackStackEntry.viewModel(
                    marshalledArgs = arrayOf(Pair(FeedConstants.Key.LoggedUser, UserView::class))
                )
                val args = navBackStackEntry.arguments
                val containsKey = remember {
                    args?.containsKey(FeedConstants.Key.LoggedUser) == true
                }
                if (args != null && !containsKey) {
                    with(context.findAppCompatActivity()) {
                        val loggedUser: UserView? =
                            intent.extras?.getParcelable(MainActivityConstants.Key.LoggedUser)
                        if (loggedUser != null) {
                            args.putParcelable(MainActivityConstants.Key.LoggedUser, loggedUser)
                        } else {
                            throw RuntimeException("null logged user")
                        }
                    }
                } else if (args == null) {
                    throw RuntimeException("null args")
                }

                val onBackPressDispatcherOwner = LocalOnBackPressedDispatcherOwner.current

                FeedScreen(
                    viewModel = viewModel,
                    onShowEventDetails = { },
                    onLoggedOut = { },
                    onNavigateBack = {
                        onBackPressDispatcherOwner
                            ?.onBackPressedDispatcher
                            ?.onBackPressed()
                    }
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        if (startNavigation.isEmpty()) {
            with(context.findAppCompatActivity()) {
                /*val loggedUser: UserView? =
                    intent.extras?.getParcelable(MainActivityConstants.Key.LoggedUser)

                val eventDetails: EventDetailsView? =
                    intent.extras?.getParcelable(MainActivityConstants.Key.EventDetails)*/

                startNavigation = intent.extras
                    ?.getInt(
                        MainActivityConstants.Key.DestinationId, R.id.signInFragment
                    )
                    ?.run(MainNavigationConstants::idToComposeNavigation)
                    ?: MainNavigationConstants.Navigation.SignIn
            }
        }
    }
}

fun NavHostController.tryGraph(): NavGraph? =
    try {
        graph
    } catch (e: Throwable) {
        null
    }