package com.sicredi.instacredi.signin

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.sicredi.core.extensions.AppBackground
import com.sicredi.core.extensions.errorContent
import com.sicredi.core.ui.compose.AppTheme
import com.sicredi.core.ui.compose.component.AppContentLoadingProgressBar
import com.sicredi.core.ui.compose.component.LocalAppModalBottomSheetState
import com.sicredi.instacredi.R
import com.sicredi.instacredi.common.extensions.ViewModelEventHandlingEffect
import com.sicredi.presenter.common.interaction.UserView
import com.sicredi.presenter.signin.SignInViewModel
import com.sicredi.presenter.signin.interaction.SignInState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    onSignedIn: (UserView) -> Unit,
    onAlreadySignedIn: (UserView) -> Unit,
    onNavigateToSignUp: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state = remember { MutableSharedFlow<SignInStateState>(replay = 0) }
    SignInScreen(
        state = state,
        onSignedIn = onSignedIn,
        onAlreadySignedIn = onAlreadySignedIn,
        onNavigateToSignUp = onNavigateToSignUp
    ) { intent ->
        when (intent) {
            is SignInIntent.SignIn -> viewModel
                .signIn(email = intent.email, password = intent.password)
        }
    }
    ViewModelEventHandlingEffect(viewModel = viewModel, context = context) { event ->
        scope.launch { state.emit(SignInStateState.ChangeState(newState = event)) }
    }
}

private sealed class SignInStateState {
    object Idle : SignInStateState()
    data class ChangeState(val newState: SignInState) : SignInStateState()
}

private sealed class SignInIntent {
    data class SignIn(val email: String, val password: String) : SignInIntent()
}

@Composable
private fun SignInScreen(
    state: Flow<SignInStateState> = flowOf(SignInStateState.Idle),
    onSignedIn: (UserView) -> Unit = {},
    onAlreadySignedIn: (UserView) -> Unit = {},
    onNavigateToSignUp: () -> Unit = {},
    onIntent: (SignInIntent) -> Unit = {},
) {
    val screenState = rememberSignInScreenState(
        onSignedIn = onSignedIn, onAlreadySignedIn = onAlreadySignedIn
    )
    SignInScreenHeader {
        SignInScreenBodyContainer {
            SignInScreenFormTextFields(screenState = screenState, onIntent = onIntent)
            Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.normal))
            SignInScreenFormButtons(
                screenState = screenState,
                onIntent = onIntent,
                onNavigateToSignUp = onNavigateToSignUp
            )
        }
    }
    if (screenState.showNotSignedInError) {
        LocalAppModalBottomSheetState.current.errorContent(
            title = stringResource(id = R.string.sign_in_invalid_user_or_pass_error_title),
            message = stringResource(id = R.string.sign_in_invalid_user_or_pass_error_description),
            positiveButtonLabel = stringResource(
                id = R.string.sign_in_invalid_user_or_pass_error_positive_action
            ),
            onPositiveClicked = {
                screenState.showNotSignedInError = false
            }
        ) {
            LaunchedEffect(screenState.showNotSignedInError) {
                if (screenState.showNotSignedInError) {
                    it.show()
                } else {
                    it.hide()
                }
            }
        }
    }
    AppContentLoadingProgressBar(visible = screenState.showLoading)
    EventHandlingEffect(state = state, handler = screenState::handleState)
}

@Composable
private fun SignInScreenHeader(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(fraction = .18f))
        val imageTint = ColorFilter
            .tint(color = AppTheme.colors.material.onSurface.copy(alpha = .12f))
        Image(
            modifier = Modifier
                .fillMaxWidth(fraction = .4f)
                .aspectRatio(ratio = 1f),
            imageVector = Icons.Default.Person,
            colorFilter = imageTint,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xtiny))
        content()
    }
}

@Composable
private fun SignInScreenBodyContainer(content: @Composable ColumnScope.() -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(width = AppTheme.Dimens.spacing.xxnormal))
        Column(modifier = Modifier.weight(weight = 1f, fill = false)) {
            content()
        }
        Spacer(modifier = Modifier.width(width = AppTheme.Dimens.spacing.xxnormal))
    }
}

@Composable
private fun SignInScreenFormTextFields(
    screenState: SignInScreenState,
    onIntent: (SignInIntent) -> Unit
) {
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = screenState.email,
        label = { Text(text = stringResource(id = R.string.email_hint)) },
        onValueChange = {
            screenState.email = it
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {
            passwordFocusRequester.requestFocus()
        }),
        singleLine = true
    )
    Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.normal))
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester = passwordFocusRequester),
        value = screenState.password,
        label = { Text(text = stringResource(id = R.string.password_hint)) },
        onValueChange = {
            screenState.password = it
        },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            onIntent(
                SignInIntent
                    .SignIn(email = screenState.email.text, password = screenState.password.text)
            )
            focusManager.clearFocus()
            defaultKeyboardAction(ImeAction.Done)
        }),
        singleLine = true,
    )
}

@Composable
private fun SignInScreenFormButtons(
    screenState: SignInScreenState,
    onIntent: (SignInIntent) -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            focusManager.clearFocus()
            onIntent(
                SignInIntent
                    .SignIn(email = screenState.email.text, password = screenState.password.text)
            )
        }
    ) {
        Text(text = stringResource(id = R.string.sign_in_action).uppercase())
    }
    OutlinedButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onNavigateToSignUp() }
    ) {
        Text(text = stringResource(id = R.string.register_action).uppercase())
    }
}

@NonRestartableComposable
@Composable
private fun EventHandlingEffect(
    state: Flow<SignInStateState>, handler: (SignInState) -> Unit
) {
    LaunchedEffect(Unit) {
        state.collect {
            if (it is SignInStateState.ChangeState) {
                handler(it.newState)
            }
        }
    }
}

// region preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun SignInScreenPreviewLight() {
    SignInScreenPreview()
}

@Composable
private fun SignInScreenPreview() {
    AppTheme {
        AppBackground {
            SignInScreen()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SignInScreenPreviewDark() {
    SignInScreenPreview()
}
// endregion