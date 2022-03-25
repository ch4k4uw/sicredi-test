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
import com.sicredi.instacredi.feed.FeedConstants
import com.sicredi.instacredi.feed.FeedScreen
import com.sicredi.instacredi.signin.SignInScreen
import com.sicredi.instacredi.signup.SignUpScreen
import com.sicredi.presenter.common.extensions.marshall
import com.sicredi.presenter.common.interaction.UserView
import com.sicredi.presenter.feed.FeedViewModel
import com.sicredi.presenter.signin.SignInViewModel
import com.sicredi.presenter.signup.SignUpViewModel
import timber.log.Timber

private object MainNavigationConstants {
    object Nav {
        object Feed {
            const val StartingRoute = "feed"
            const val Route = "$StartingRoute?" +
                    "${FeedConstants.Key.LoggedUser}={${FeedConstants.Key.LoggedUser}}"
            fun build(user: UserView): String =
                "$StartingRoute?${FeedConstants.Key.LoggedUser}=${user.marshall()}"
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
            else -> Feed.StartingRoute
        }
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
                val onBackPressDispatcherOwner = LocalOnBackPressedDispatcherOwner.current

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
                    onShowEventDetails = { Timber.i(it.toString()) },
                    onLoggedOut = ::navToSignIn,
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