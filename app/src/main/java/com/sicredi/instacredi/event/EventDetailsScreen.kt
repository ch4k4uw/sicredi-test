package com.sicredi.instacredi.event

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.sicredi.core.ui.compose.component.AppBackground
import com.sicredi.core.extensions.ConnectivityErrorContent
import com.sicredi.core.extensions.GenericErrorContent
import com.sicredi.core.extensions.infoContent
import com.sicredi.core.ui.compose.component.AppCollapsingTopBarScaffold
import com.sicredi.core.ui.compose.AppTheme
import com.sicredi.core.ui.compose.component.AppContentLoadingProgressBar
import com.sicredi.core.ui.compose.component.AppImage
import com.sicredi.core.ui.compose.component.LocalAppModalBottomSheetState
import com.sicredi.instacredi.R
import com.sicredi.instacredi.common.ProfileBottomSheet
import com.sicredi.instacredi.common.extensions.ViewModelEventHandlingEffect
import com.sicredi.presenter.common.interaction.EventDetailsView
import com.sicredi.presenter.common.interaction.UserView
import com.sicredi.presenter.event.EventDetailsViewModel
import com.sicredi.presenter.event.interaction.EventDetailsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun EventDetailsScreen(
    viewModel: EventDetailsViewModel,
    userView: UserView,
    onLoggedOut: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state = remember { MutableSharedFlow<EventDetailsStateState>(replay = 0) }
    EventDetailsScreen(
        state = state,
        userView = userView,
        onLoggedOut = onLoggedOut,
        onNavigateBack = onNavigateBack,
        onIntent = { intent ->
            when (intent) {
                EventDetailsIntent.PerformCheckIn -> viewModel.performCheckIn()
                EventDetailsIntent.ShareEvent -> viewModel.shareEvent()
                EventDetailsIntent.ShowGoogleMaps -> viewModel.showGoogleMaps()
                EventDetailsIntent.Logout -> viewModel.logout()
            }
        }
    )
    ViewModelEventHandlingEffect(viewModel = viewModel, context = context) { event ->
        scope.launch { state.emit(EventDetailsStateState.ChangeState(newState = event)) }
    }
}

private sealed class EventDetailsStateState {
    object Idle : EventDetailsStateState()
    data class ChangeState(val newState: EventDetailsState) : EventDetailsStateState()
}

private sealed class EventDetailsIntent {
    object PerformCheckIn : EventDetailsIntent()
    object ShowGoogleMaps : EventDetailsIntent()
    object ShareEvent : EventDetailsIntent()
    object Logout : EventDetailsIntent()
}

@Composable
private fun EventDetailsScreen(
    state: Flow<EventDetailsStateState> = flowOf(EventDetailsStateState.Idle),
    userView: UserView = UserView(id = "", name = "", email = ""),
    onLoggedOut: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onIntent: (EventDetailsIntent) -> Unit = {},
) {
    val context = LocalContext.current
    val screenState = rememberEventDetailsScreenState(
        onShowGoogleMaps = {
            context.startActivity(it)
        },
        onShareEvent = {
            context.startActivity(it)
        },
        onLoggedOut = onLoggedOut
    )

    BoxWithConstraints {
        AppCollapsingTopBarScaffold(
            title = {
                Text(
                    text = screenState.details.title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    softWrap = false
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                }
            },
            actions = {
                IconButton(onClick = { screenState.showProfileDialog = true }) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "")
                }
                IconButton(onClick = { screenState.showEventOptionsDialog = true }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
                }
            },
            fab = {
                FloatingActionButton(onClick = { onIntent(EventDetailsIntent.ShareEvent) }) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "")
                }
            },
            fixedTopBar = false,
            saveStateEnabled = true,
            barContentHeight = maxWidth * .7f,
            barContent = {
                AppImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    uri = Uri.parse(screenState.details.image),
                    contentScale = ContentScale.Crop
                )
            }
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .padding(horizontal = AppTheme.Dimens.spacing.normal)
                    .verticalScroll(state = scrollState)
            ) {
                Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.normal))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = screenState.details.title,
                    style = AppTheme.typography.material.h6
                )
                Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xtiny))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = AppTheme.Dimens.spacing.xxnormal),
                    text = screenState.details.description,
                    style = AppTheme.typography.material.subtitle1
                )
            }
        }
    }

    EventDetailsErrorBottomSheet(screenState = screenState, onIntent = onIntent)
    EventDetailsSuccessBottomSheet(screenState = screenState)
    EventDetailsProfileBottomSheet(
        userView = userView,
        screenState = screenState,
        onIntent = onIntent
    )
    EventDetailsEventOptions(screenState = screenState, onIntent = onIntent)
    AppContentLoadingProgressBar(visible = screenState.showLoading)
    EventHandlingEffect(state = state, handler = screenState::handleState)
}

@Composable
private fun EventDetailsErrorBottomSheet(
    screenState: EventDetailsScreenState, onIntent: (EventDetailsIntent) -> Unit
) {
    val errorCode by derivedStateOf {
        with(screenState) {
            val b1 = if (showCheckInConnectionError) 1 else 0
            val b2 = if (showEventSharingConnectionError) 1 shl 1 else 0
            val b3 = if (showCheckInConnectionError) 1 shl 2 else 0
            val b4 = if (showEventSharingConnectionError) 1 shl 3 else 0
            b1 or b2 or b3 or b4
        }
    }

    fun hideGenericError() {
        screenState.showCheckInGenericError = false
        screenState.showEventSharingGenericError = false
    }

    fun hideConnectivityError() {
        screenState.showCheckInConnectionError = false
        screenState.showEventSharingConnectionError = false
    }

    if (errorCode != 0) {
        with(screenState) {
            if (errorCode and (1 or (1 shl 1)) != 0) {
                LocalAppModalBottomSheetState.current.GenericErrorContent(
                    onPositiveClicked = {
                        if (showCheckInGenericError) {
                            onIntent(EventDetailsIntent.PerformCheckIn)
                        } else {
                            onIntent(EventDetailsIntent.ShareEvent)
                        }
                        hideGenericError()
                    },
                    onNegativeClicked = ::hideGenericError,
                    confirmStateChange = {
                        hideGenericError()
                        true
                    }
                ) {
                    LaunchedEffect(errorCode) {
                        if (errorCode and (1 or (1 shl 1)) != 0) {
                            it.show()
                        } else {
                            it.hide()
                        }
                    }
                }
            } else {
                LocalAppModalBottomSheetState.current.ConnectivityErrorContent(
                    onPositiveClicked = {
                        if (showCheckInConnectionError) {
                            onIntent(EventDetailsIntent.PerformCheckIn)
                        } else {
                            onIntent(EventDetailsIntent.ShareEvent)
                        }
                        hideConnectivityError()
                    },
                    onNegativeClicked = ::hideConnectivityError,
                    confirmStateChange = {
                        hideConnectivityError()
                        true
                    }
                ) {
                    LaunchedEffect(errorCode) {
                        if (errorCode and ((1 shl 2) or (1 shl 3)) != 0) {
                            it.show()
                        } else {
                            it.hide()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EventDetailsSuccessBottomSheet(
    screenState: EventDetailsScreenState
) {
    if (screenState.showCheckedInSuccess) {
        val dialogResources = object {
            val title = R.string.event_details_successful_checked_in_message_title
            val message = R.string.event_details_successful_checked_in_message_description
            val positiveLabel = R.string.event_details_successful_checked_in_message_primary_button
        }
        LocalAppModalBottomSheetState.current.infoContent(
            title = stringResource(id = dialogResources.title),
            message = stringResource(id = dialogResources.message),
            positiveButtonLabel = stringResource(id = dialogResources.positiveLabel),
            onPositiveClicked = { screenState.showCheckedInSuccess = false },
            confirmStateChange = {
                if (it == ModalBottomSheetValue.Hidden) {
                    screenState.showCheckedInSuccess = false
                }
                true
            },
        ) {
            LaunchedEffect(screenState.showCheckedInSuccess) {
                if (screenState.showCheckedInSuccess) {
                    it.show()
                } else {
                    it.hide()
                }
            }
        }
    }
}

@Composable
private fun EventDetailsProfileBottomSheet(
    userView: UserView,
    screenState: EventDetailsScreenState,
    onIntent: (EventDetailsIntent) -> Unit
) {
    if (screenState.showProfileDialog) {
        LocalAppModalBottomSheetState.current.ProfileBottomSheet(
            name = userView.name, email = userView.email, onLogoutClick = {
                screenState.showProfileDialog = false
                onIntent(EventDetailsIntent.Logout)
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
}

@Composable
private fun EventDetailsEventOptions(
    screenState: EventDetailsScreenState, onIntent: (EventDetailsIntent) -> Unit
) {
    if (screenState.showEventOptionsDialog) {
        LocalAppModalBottomSheetState.current.EventOptions(
            onCheckInClick = {
                onIntent(EventDetailsIntent.PerformCheckIn)
                screenState.showEventOptionsDialog = false
            },
            onShareClick = {
                onIntent(EventDetailsIntent.ShareEvent)
                screenState.showEventOptionsDialog = false
            },
            onGoogleMapsClick = {
                onIntent(EventDetailsIntent.ShowGoogleMaps)
                screenState.showEventOptionsDialog = false
            },
            stateChange = {
                if (it == ModalBottomSheetValue.Hidden) {
                    screenState.showEventOptionsDialog = false
                }
                true
            }
        ) {
            LaunchedEffect(screenState.showEventOptionsDialog) {
                if (screenState.showEventOptionsDialog)
                    it.show()
                else
                    it.hide()
            }
        }
    }
}

@NonRestartableComposable
@Composable
private fun EventHandlingEffect(
    state: Flow<EventDetailsStateState>, handler: (EventDetailsState) -> Unit
) {
    LaunchedEffect(Unit) {
        state.collect {
            if (it is EventDetailsStateState.ChangeState) {
                handler(it.newState)
            }
        }
    }
}

// region preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun EventDetailsScreenPreviewLight() {
    EventDetailsScreenPreview()
}

@Composable
private fun EventDetailsScreenPreview() {
    AppTheme {
        AppBackground {
            EventDetailsScreen(
                state = flowOf(
                    value = EventDetailsStateState.ChangeState(
                        newState = EventDetailsState.DisplayDetails(
                            details = EventDetailsView(
                                title = "Feira de adoção de animais na Redenção",
                                description = "O Patas Dadas estará na Redenção, nesse " +
                                        "domingo, com cães para adoção e produtos à venda!\n\n" +
                                        "Na ocasião, teremos bottons, bloquinhos e camisetas!\n\n" +
                                        "Traga seu Pet, os amigos e o chima, e venha aproveitar " +
                                        "esse dia de sol com a gente e com alguns de nossos " +
                                        "peludinhos - que estarão prontinhos para ganhar o ♥ " +
                                        "de um humano bem legal pra chamar de seu. \n\n" +
                                        "Aceitaremos todos os tipos de doação:\n" +
                                        "- guias e coleiras em bom estado\n" +
                                        "- ração (as que mais precisamos no momento são sênior e" +
                                        " filhote)\n" +
                                        "- roupinhas \n" +
                                        "- cobertas \n" +
                                        "- remédios dentro do prazo de validade"
                            )
                        )
                    )
                ),
                userView = UserView(
                    id = "", name = "Pedro Motta", email = "pedro.motta@avenuecode.com"
                )
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EventDetailsScreenPreviewDark() {
    EventDetailsScreenPreview()
}
// endregion