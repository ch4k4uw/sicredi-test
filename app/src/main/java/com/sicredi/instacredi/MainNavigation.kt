package com.sicredi.instacredi

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sicredi.instacredi.common.extensions.viewModel
import com.sicredi.instacredi.event.EventDetailsConstants
import com.sicredi.instacredi.event.EventDetailsScreen
import com.sicredi.instacredi.feed.FeedConstants
import com.sicredi.instacredi.feed.FeedScreen
import com.sicredi.instacredi.signin.SignInScreen
import com.sicredi.instacredi.signup.SignUpScreen
import com.sicredi.presenter.common.extensions.marshall
import com.sicredi.presenter.common.interaction.EventDetailsView
import com.sicredi.presenter.common.interaction.UserView
import com.sicredi.presenter.event.EventDetailsViewModel
import com.sicredi.presenter.feed.FeedViewModel
import com.sicredi.presenter.signin.SignInViewModel
import com.sicredi.presenter.signup.SignUpViewModel

private object MainNavigationConstants {
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

        fun idToStartingRoute(id: Int) = when (id) {
            R.id.signInFragment -> SignIn.StartingRoute
            R.id.signUpFragment -> SignUp.StartingRoute
            R.id.feedFragment -> Feed.StartingRoute
            R.id.eventDetailsFragment -> EventDetails.StartingRoute
            else -> SignIn.StartingRoute
        }
    }
}

@Composable
fun MainNavigation() {
    val context = LocalContext.current
    val navController = rememberNavController()
    var startNavigation by rememberSaveable { mutableStateOf("") }
    val onBackPressDispatcherOwner = LocalOnBackPressedDispatcherOwner.current

    fun performBackPressed() {
        onBackPressDispatcherOwner
            ?.onBackPressedDispatcher
            ?.onBackPressed()
    }

    if (startNavigation.isNotEmpty()) {
        NavHost(
            navController = navController,
            startDestination = navController.tryGraph()?.startDestinationRoute ?: startNavigation
        ) {
            composable(
                route = MainNavigationConstants.Nav.SignIn.Route,
            ) { navBackStackEntry ->
                val viewModel: SignInViewModel = navBackStackEntry.viewModel()

                fun navToFeed(user: UserView) {
                    navController
                        .navigate(route = MainNavigationConstants.Nav.Feed.build(user = user)) {
                            popUpTo(route = MainNavigationConstants.Nav.SignIn.Route) {
                                inclusive = true
                            }
                        }
                }

                fun navToSignUp() {
                    navController
                        .navigate(route = MainNavigationConstants.Nav.SignUp.Route)
                }

                SignInScreen(
                    viewModel = viewModel,
                    onSignedIn = ::navToFeed,
                    onAlreadySignedIn = ::navToFeed,
                    onNavigateToSignUp = ::navToSignUp
                )
            }
            composable(
                route = MainNavigationConstants.Nav.SignUp.Route,
            ) { navBackStackEntry ->
                val viewModel: SignUpViewModel = navBackStackEntry.viewModel()
                fun navToFeed(user: UserView) {
                    navController
                        .navigate(route = MainNavigationConstants.Nav.Feed.build(user = user)) {
                            popUpTo(route = MainNavigationConstants.Nav.SignUp.Route) {
                                inclusive = true
                            }
                        }
                }
                SignUpScreen(
                    viewModel = viewModel,
                    onSignedUp = { navToFeed(user = it) },
                    onNavigationBack = {
                        navController.navigateUp()
                    }
                )
            }
            composable(
                route = MainNavigationConstants.Nav.Feed.StartingRoute
            ) {
                val loggedUser = with(context.findAppCompatActivity()) {
                    intent.extras?.getParcelable<UserView>(MainActivityConstants.Key.LoggedUser)
                        ?: throw RuntimeException("null logged user")
                }

                LaunchedEffect(Unit) {
                    navController
                        .navigate(
                            route = MainNavigationConstants.Nav.Feed.build(user = loggedUser)
                        ) {
                            popUpTo(route = MainNavigationConstants.Nav.Feed.StartingRoute) {
                                inclusive = true
                            }
                        }
                }
            }
            composable(
                route = MainNavigationConstants.Nav.Feed.Route,
                arguments = listOf(
                    navArgument(name = FeedConstants.Key.LoggedUser) {
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { navBackStackEntry ->
                val viewModel: FeedViewModel = navBackStackEntry.viewModel(
                    marshalledArgs = arrayOf(Pair(FeedConstants.Key.LoggedUser, UserView::class))
                )
                val args = navBackStackEntry.arguments ?: throw RuntimeException("null args")

                fun navToSignIn() {
                    navController
                        .navigate(route = MainNavigationConstants.Nav.SignIn.Route) {
                            popUpTo(route = MainNavigationConstants.Nav.Feed.Route) {
                                inclusive = true
                            }
                        }
                }

                FeedScreen(
                    viewModel = viewModel,
                    userView = args.getParcelable(FeedConstants.Key.LoggedUser)!!,
                    onShowEventDetails = {
                        val destination = MainNavigationConstants.Nav.EventDetails.build(
                            user = args.getParcelable(FeedConstants.Key.LoggedUser)!!,
                            details = it
                        )
                        navController.navigate(route = destination)
                    },
                    onLoggedOut = ::navToSignIn,
                    onNavigateBack = ::performBackPressed
                )
            }
            composable(
                route = MainNavigationConstants.Nav.EventDetails.StartingRoute,
            ) {
                val intentData = with(context.findAppCompatActivity()) {
                    object {
                        val loggedUser = intent
                            .extras
                            ?.getParcelable<UserView>(MainActivityConstants.Key.LoggedUser)
                            ?: throw RuntimeException("null logged user")
                        val details = intent
                            .extras
                            ?.getParcelable<EventDetailsView>(MainActivityConstants.Key.EventDetails)
                            ?: throw RuntimeException("null event details")
                    }
                }
                LaunchedEffect(Unit) {
                    navController
                        .navigate(
                            route = MainNavigationConstants.Nav.EventDetails.build(
                                user = intentData.loggedUser,
                                details = intentData.details
                            )
                        ) {
                            val currRoute = MainNavigationConstants.Nav.EventDetails.StartingRoute
                            popUpTo(route = currRoute) {
                                inclusive = true
                            }
                        }
                }
            }
            composable(
                route = MainNavigationConstants.Nav.EventDetails.Route,
                arguments = listOf(
                    navArgument(name = EventDetailsConstants.Key.LoggedUser) {
                        nullable = true
                        defaultValue = null
                    },
                    navArgument(name = EventDetailsConstants.Key.Details) {
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { navBackStackEntry ->
                val viewModel: EventDetailsViewModel = navBackStackEntry.viewModel(
                    marshalledArgs = arrayOf(
                        Pair(EventDetailsConstants.Key.LoggedUser, UserView::class),
                        Pair(EventDetailsConstants.Key.Details, EventDetailsView::class),
                    )
                )
                val args = navBackStackEntry.arguments ?: throw RuntimeException("null args")

                fun navToSignIn() {
                    navController
                        .navigate(route = MainNavigationConstants.Nav.SignIn.Route) {
                            popUpTo(route = MainNavigationConstants.Nav.EventDetails.Route) {
                                inclusive = true
                            }
                        }
                }

                EventDetailsScreen(
                    viewModel = viewModel,
                    userView = args.getParcelable(EventDetailsConstants.Key.LoggedUser)!!,
                    onLoggedOut = ::navToSignIn,
                    onNavigateBack = ::performBackPressed
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        if (startNavigation.isEmpty()) {
            with(context.findAppCompatActivity()) {
                startNavigation = intent.extras
                    ?.getInt(
                        MainActivityConstants.Key.DestinationId, R.id.signInFragment
                    )
                    ?.run(MainNavigationConstants.Nav::idToStartingRoute)
                    ?: MainNavigationConstants.Nav.SignIn.StartingRoute
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