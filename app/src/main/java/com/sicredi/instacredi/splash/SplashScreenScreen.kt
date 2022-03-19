package com.sicredi.instacredi.splash

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sicredi.core.ui.compose.AppTheme
import com.sicredi.instacredi.R
import com.sicredi.instacredi.findAppCompatActivity
import com.sicredi.instacredi.startMainActivityForEventDetails
import com.sicredi.instacredi.startMainActivityForFeedFragment
import com.sicredi.instacredi.startMainActivityForSignInFragment
import com.sicredi.presenter.splash.SplashScreenViewModel
import com.sicredi.presenter.splash.interaction.SplashScreenState

@Composable
fun SplashScreenScreen(viewModel: SplashScreenViewModel) {
    val systemUiController = rememberSystemUiController()
    val context = LocalContext.current

    Screen()

    EventHandlingEffect(viewModel = viewModel, context = context)
    TransparentSystemBarEffect(systemUiController = systemUiController)
}

@Composable
private fun Screen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(fraction = .43f))
        AppNameText()
        StatusText()
    }
}

@Composable
private fun AppNameText() {
    Text(
        text = stringResource(R.string.app_name),
        style = AppTheme.typography.material.h3,
        color = AppTheme.colors.material.primaryVariant
    )
}

@Composable
private fun StatusText() {
    Text(
        text = stringResource(R.string.please_wait_prompt),
        style = AppTheme.typography.material.subtitle1,
        color = AppTheme.colors.material.onPrimary
    )
}

@NonRestartableComposable
@Composable
private fun EventHandlingEffect(viewModel: SplashScreenViewModel, context: Context) {
    LaunchedEffect(context) {
        val activity = context.findAppCompatActivity()
        viewModel.state.collect { state ->
            handleState(viewModel = viewModel, activity = activity, state = state)
        }
    }
}

private fun handleState(
    viewModel: SplashScreenViewModel,
    activity: AppCompatActivity,
    state: SplashScreenState
) {
    with(activity) {
        when (state) {
            is SplashScreenState.NotInitialized -> throw state.cause
            is SplashScreenState.EventDetailsNotLoaded -> throw state.cause
            is SplashScreenState.ShowFeedScreen -> {
                val data = intent.data
                val eventId = data?.getQueryParameter("eventId")
                if (eventId != null) {
                    viewModel.findDetails(user = state.user, eventId = eventId)
                } else {
                    startMainActivityForFeedFragment(user = state.user)
                }
            }
            SplashScreenState.ShowSignInScreen -> {
                startMainActivityForSignInFragment()
            }
            is SplashScreenState.EventDetailsSuccessfulLoaded -> {
                startMainActivityForEventDetails(
                    user = state.user, eventDetails = state.eventDetails
                )
            }
        }
    }
}

@NonRestartableComposable
@Composable
fun TransparentSystemBarEffect(systemUiController: SystemUiController) {
    SideEffect { systemUiController.setSystemBarsColor(color = Color.Transparent) }
}