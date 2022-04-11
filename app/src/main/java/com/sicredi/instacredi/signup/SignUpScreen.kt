package com.sicredi.instacredi.signup

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.sicredi.core.ui.compose.component.AppBackground
import com.sicredi.core.ui.compose.AppTheme
import com.sicredi.core.ui.compose.LocalAppInsetsPaddingValues
import com.sicredi.core.ui.compose.component.AppContentLoadingProgressBar
import com.sicredi.instacredi.R
import com.sicredi.instacredi.common.extensions.RestoreWindowBarsEffect
import com.sicredi.instacredi.common.extensions.ViewModelEventHandlingEffect
import com.sicredi.presenter.common.interaction.UserView
import com.sicredi.presenter.signup.SignUpViewModel
import com.sicredi.presenter.signup.interaction.SignUpState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    onSignedUp: (UserView) -> Unit,
    onNavigationBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state = remember { MutableSharedFlow<SignUpStateState>(replay = 0) }
    SignUpScreen(
        state = state,
        onSignedUp = onSignedUp,
        onNavigationBack = onNavigationBack
    ) { intent ->
        if (intent is SignUpIntent.SignUp) {
            viewModel.signUp(
                name = intent.name,
                email = intent.email,
                password = intent.password,
            )
        }
    }
    ViewModelEventHandlingEffect(viewModel = viewModel, context = context) { event ->
        scope.launch { state.emit(SignUpStateState.ChangeState(newState = event)) }
    }
}

private sealed class SignUpStateState {
    object Idle : SignUpStateState()
    data class ChangeState(val newState: SignUpState) : SignUpStateState()
}

private sealed class SignUpIntent {
    data class SignUp(val name: String, val email: String, val password: String) : SignUpIntent()
}

@Composable
private fun SignUpScreen(
    state: Flow<SignUpStateState> = flowOf(SignUpStateState.Idle),
    onSignedUp: (UserView) -> Unit = {},
    onNavigationBack: () -> Unit = {},
    onIntent: (SignUpIntent) -> Unit = {},
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val screenState = rememberSignUpScreenState(onSignedUp = onSignedUp)
    with(screenState) {
        val emailFocusRequester = remember { FocusRequester() }
        val pass1FocusRequester = remember { FocusRequester() }
        val pass2FocusRequester = remember { FocusRequester() }

        fun getString(@StringRes id: Int): String =
            context.getString(id)

        SignUpScreenHeader(onNavigateBack = onNavigationBack) {
            val nameError by remember(showInvalidNameError, context) {
                derivedStateOf {
                    if (showInvalidNameError) {
                        getString(R.string.sign_up_invalid_name_error_prompt)
                    } else {
                        null
                    }
                }
            }
            val emailError by remember(
                showInvalidEmailError,
                showDuplicatedEmailError,
                context,
            ) {
                derivedStateOf {
                    when {
                        showInvalidEmailError ->
                            getString(R.string.sign_up_invalid_email_error_prompt)
                        showDuplicatedEmailError ->
                            getString(R.string.sign_up_duplicated_email_error_prompt)
                        else -> null
                    }
                }
            }
            val passwordError by remember(
                showInvalidPasswordError,
                showPasswordMatchError,
                context,
            ) {
                derivedStateOf {
                    when {
                        showInvalidPasswordError ->
                            getString(R.string.sign_up_invalid_password_error_prompt)
                        showPasswordMatchError ->
                            getString(R.string.sign_up_passwords_must_match_error_prompt)
                        else -> null
                    }
                }
            }

            fun hideNameErrors() { showInvalidNameError = false }
            fun hideEmailErrors() {
                showInvalidEmailError = false; showDuplicatedEmailError = false
            }
            fun hidePasswordErrors() {
                showInvalidPasswordError = false; showPasswordMatchError = false
            }

            fun submit() {
                val pass1 = password1.text
                val pass2 = password2.text
                if (assertPasswordMatch(password1 = pass1, password2 = pass2)) {
                    onIntent(
                        SignUpIntent.SignUp(name = name.text, email = email.text, password = pass1)
                    )
                }
            }

            SignUpScreenFormTextField(
                value = name,
                onTextChanged = { hideNameErrors(); name = it },
                label = stringResource(id = R.string.name_hint),
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                nextFocusRequester = emailFocusRequester,
                error = nameError,
            )
            SignUpScreenFormTextField(
                value = email,
                onTextChanged = { hideEmailErrors(); email = it },
                label = stringResource(id = R.string.email_hint),
                keyboardType = KeyboardType.Email,
                focusRequester = emailFocusRequester,
                nextFocusRequester = pass1FocusRequester,
                error = emailError,
            )
            SignUpScreenFormTextField(
                value = password1,
                onTextChanged = { hidePasswordErrors(); password1 = it },
                label = stringResource(id = R.string.password_hint),
                keyboardType = KeyboardType.Password,
                focusRequester = pass1FocusRequester,
                nextFocusRequester = pass2FocusRequester,
                error = passwordError,
            )
            SignUpScreenFormTextField(
                value = password2,
                onTextChanged = { hidePasswordErrors(); password2 = it },
                label = stringResource(id = R.string.password_hint),
                onDone = ::submit,
                keyboardType = KeyboardType.Password,
                focusRequester = pass2FocusRequester,
                error = passwordError
            )
            SignUpScreenSubmitButton {
                focusManager.clearFocus()
                submit()
            }
        }
        AppContentLoadingProgressBar(visible = screenState.showLoading)
        EventHandlingEffect(state = state, handler = ::handleState)
        RestoreWindowBarsEffect()
    }
}

@Composable
private fun SignUpScreenHeader(
    onNavigateBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(LocalAppInsetsPaddingValues.current.paddingValues.value)
    ) {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.app_name)) },
            backgroundColor = AppTheme.colors.material.primary,
            navigationIcon = {
                IconButton(onClick = { onNavigateBack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                }
            }
        )
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(horizontal = AppTheme.Dimens.spacing.xxnormal)
                .weight(weight = 1f, fill = true)
                .verticalScroll(state = scrollState)
        ) {
            content()
        }
    }
}

@Composable
private fun SignUpScreenFormTextField(
    value: TextFieldValue,
    onTextChanged: (TextFieldValue) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    error: String? = null,
    onDone: (() -> Unit)? = null,
    focusRequester: FocusRequester? = null,
    nextFocusRequester: FocusRequester? = null,
) {
    val marginTop = if (focusRequester == null) {
        AppTheme.Dimens.spacing.xxnormal
    } else {
        AppTheme.Dimens.spacing.tiny
    }
    val focusManager = LocalFocusManager.current
    Spacer(modifier = Modifier.height(height = marginTop))
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                focusRequester
                    ?.let { Modifier.focusRequester(focusRequester = focusRequester) } ?: Modifier
            ),
        value = value,
        onValueChange = onTextChanged,
        label = { Text(text = label) },
        visualTransformation = if (keyboardType == KeyboardType.Password) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            capitalization = capitalization,
            keyboardType = keyboardType,
            imeAction = if (nextFocusRequester == null) ImeAction.Done else ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                nextFocusRequester?.requestFocus()
            },
            onDone = {
                focusManager.clearFocus()
                onDone?.invoke()
            }
        ),
        singleLine = true
    )
    Spacer(modifier = Modifier.height(AppTheme.Dimens.spacing.tiny))
    Text(
        text = error ?: "",
        style = AppTheme.typography.material.body2,
        color = AppTheme.colors.material.error
    )
}

@Composable
private fun ColumnScope.SignUpScreenSubmitButton(onClick: () -> Unit) {
    Spacer(modifier = Modifier.weight(weight = 1f))
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Text(text = stringResource(id = R.string.submit_action).uppercase())
    }
    Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xxnormal))
}

@NonRestartableComposable
@Composable
private fun EventHandlingEffect(
    state: Flow<SignUpStateState>, handler: (SignUpState) -> Unit
) {
    LaunchedEffect(Unit) {
        state.collect {
            if (it is SignUpStateState.ChangeState) {
                handler(it.newState)
            }
        }
    }
}

// region preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun SignUpScreenPreviewLight() {
    SignUpScreenPreview()
}

@Composable
private fun SignUpScreenPreview() {
    AppTheme {
        AppBackground {
            SignUpScreen()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SignUpScreenPreviewDark() {
    SignUpScreenPreview()
}
// endregion