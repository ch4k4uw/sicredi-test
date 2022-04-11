package com.sicredi.instacredi.feed

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sicredi.core.ui.compose.component.AppBackground
import com.sicredi.core.ui.compose.component.AppScrollableTopBarScaffold
import com.sicredi.core.ui.compose.AppTheme
import com.sicredi.core.ui.compose.LocalAppInsetsPaddingValues
import com.sicredi.core.ui.compose.component.AppContentLoadingProgressBar
import com.sicredi.core.ui.compose.component.LocalAppModalBottomSheetState
import com.sicredi.instacredi.R
import com.sicredi.instacredi.common.ProfileBottomSheet
import com.sicredi.instacredi.common.extensions.RestoreWindowBarsEffect
import com.sicredi.instacredi.common.extensions.ViewModelEventHandlingEffect
import com.sicredi.instacredi.feed.component.FeedListItem
import com.sicredi.presenter.common.interaction.EventDetailsView
import com.sicredi.presenter.common.interaction.UserView
import com.sicredi.presenter.feed.FeedViewModel
import com.sicredi.presenter.feed.interaction.EventHeadView
import com.sicredi.presenter.feed.interaction.FeedState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
    userView: UserView,
    onShowEventDetails: (EventDetailsView) -> Unit,
    onLoggedOut: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state = remember { MutableSharedFlow<FeedStateState>(replay = 0) }
    FeedScreen(
        state = state,
        userView = userView,
        onShowEventDetails = onShowEventDetails,
        onLoggedOut = onLoggedOut,
        onNavigateBack = onNavigateBack
    ) { intent ->
        when (intent) {
            FeedIntent.Load -> viewModel.loadFeed()
            is FeedIntent.FindDetails -> viewModel.findDetails(id = intent.id)
            FeedIntent.Logout -> viewModel.logout()
        }
    }
    ViewModelEventHandlingEffect(viewModel = viewModel, context = context) { event ->
        scope.launch { state.emit(FeedStateState.ChangeState(newState = event)) }
    }
}

private sealed class FeedStateState {
    object Idle : FeedStateState()
    data class ChangeState(val newState: FeedState) : FeedStateState()
}

private sealed class FeedIntent {
    object Load : FeedIntent()
    data class FindDetails(val id: String) : FeedIntent()
    object Logout : FeedIntent()
}

@Composable
private fun FeedScreen(
    state: Flow<FeedStateState> = flowOf(FeedStateState.Idle),
    userView: UserView = UserView(id = "", name = "", email = ""),
    onShowEventDetails: (EventDetailsView) -> Unit = {},
    onLoggedOut: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onIntent: (FeedIntent) -> Unit = {},
) {
    var loadFeed by rememberSaveable { mutableStateOf(true) }
    val screenState = rememberFeedScreenState(
        onShowEventDetails = onShowEventDetails, onLoggedOut = onLoggedOut
    )

    AppScrollableTopBarScaffold(
        modifier = Modifier
            .padding(LocalAppInsetsPaddingValues.current.paddingValues.value),
        title = { Text(text = stringResource(id = R.string.app_name)) },
        backgroundColor = AppTheme.colors.material.primary,
        navigationIcon = {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
            }
        },
        actions = {
            IconButton(onClick = { screenState.showProfileDialog = true }) {
                Icon(imageVector = Icons.Filled.Person, contentDescription = "")
            }
        }
    ) {
        val events = screenState.events
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(count = events.size, key = { events[it].id }) { index ->
                val event = events[index]
                FeedListItem(
                    image = event.image,
                    title = event.title,
                    price = event.price,
                    onClick = { onIntent(FeedIntent.FindDetails(id = event.id)) }
                )
            }
        }
    }

    if (screenState.showProfileDialog) {
        LocalAppModalBottomSheetState.current.ProfileBottomSheet(
            name = userView.name, email = userView.email, onLogoutClick = {
                screenState.showProfileDialog = false
                onIntent(FeedIntent.Logout)
            }, confirmStateChange = {
                if (it == ModalBottomSheetValue.Hidden) {
                    screenState.showProfileDialog = false
                    false
                } else {
                    true
                }
            }
        ) {
            LaunchedEffect(screenState.showProfileDialog) {
                if (screenState.showProfileDialog) {
                    it.show()
                } else {
                    it.hide()
                }
            }
        }
    }

    AppContentLoadingProgressBar(visible = screenState.showLoading)
    EventHandlingEffect(state = state, handler = screenState::handleState)
    RestoreWindowBarsEffect()
    SideEffect {
        if (loadFeed) {
            loadFeed = false
            onIntent(FeedIntent.Load)
        }
    }
}

@NonRestartableComposable
@Composable
private fun EventHandlingEffect(
    state: Flow<FeedStateState>, handler: (FeedState) -> Unit
) {
    LaunchedEffect(Unit) {
        state.collect {
            if (it is FeedStateState.ChangeState) {
                handler(it.newState)
            }
        }
    }
}

// region preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun FeedScreenPreviewLight() {
    FeedScreenPreview()
}

@Composable
private fun FeedScreenPreview() {
    val state = FeedStateState.ChangeState(
        newState = FeedState.FeedSuccessfulLoaded(
            eventHeads = listOf(
                EventHeadView(
                    id = "a",
                    title = "Some huge text to see how some similar text will appear in this layout. Ok, " +
                            "for smartphone it's already big enough.",
                    price = 42.50,
                    date = LocalDateTime.MIN,
                    image = "",
                    lat = Double.NaN,
                    long = Double.NaN
                ),
                EventHeadView(
                    id = "b",
                    title = "Some other text, but now not so much huge.",
                    price = 52.50,
                    date = LocalDateTime.MIN,
                    image = "",
                    lat = Double.NaN,
                    long = Double.NaN
                )
            ).dropLast(1)
        )
    )
    AppTheme {
        AppBackground {
            FeedScreen(
                state = flowOf(state),
                userView = UserView(
                    id = "", name = "Pedro Motta", email = "pedro.motta@avenuecode.com"
                )
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FeedScreenPreviewDark() {
    FeedScreenPreview()
}
// endregion