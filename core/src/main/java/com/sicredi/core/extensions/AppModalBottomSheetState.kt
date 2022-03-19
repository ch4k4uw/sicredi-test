package com.sicredi.core.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.insets.navigationBarsPadding
import com.sicredi.core.R
import com.sicredi.core.ui.compose.AppTheme
import com.sicredi.core.ui.compose.component.AppModalBottomSheetState
import com.sicredi.core.ui.compose.component.AppRoundedCornerButton
import com.sicredi.core.ui.compose.component.AppRoundedCornerOutlinedButton

@Composable
fun AppModalBottomSheetState.GenericErrorContent(
    onPositiveClicked: (() -> Unit)? = null,
    onNegativeClicked: (() -> Unit)? = null,
    confirmStateChange: ((ModalBottomSheetValue) -> Boolean)? = null,
    interaction: @Composable (ModalBottomSheetState) -> Unit
) {
    val context = LocalContext.current
    errorContent(
        title = context.getString(R.string.generic_error_title),
        message = context.getString(R.string.generic_error_message),
        positiveButtonLabel = onPositiveClicked?.let {
            context.getString(R.string.generic_error_positive_button)
        },
        onPositiveClicked = onPositiveClicked,
        negativeButtonLabel = onNegativeClicked?.let {
            context.getString(R.string.generic_error_negative_button)
        },
        onNegativeClicked = onNegativeClicked,
        confirmStateChange = confirmStateChange,
        interaction = interaction
    )
}

fun AppModalBottomSheetState.errorContent(
    title: String,
    message: String,
    positiveButtonLabel: String? = null,
    onPositiveClicked: (() -> Unit)? = null,
    negativeButtonLabel: String? = null,
    onNegativeClicked: (() -> Unit)? = null,
    confirmStateChange: ((ModalBottomSheetValue) -> Boolean)? = null,
    interaction: @Composable (ModalBottomSheetState) -> Unit
) {
    sheetContent(stateChange = confirmStateChange) {
        SheetContent(
            type = ModalBottomSheetAlertType.Error,
            title = title,
            message = message,
            positiveButtonLabel = positiveButtonLabel,
            onPositiveClicked = onPositiveClicked,
            negativeButtonLabel = negativeButtonLabel,
            onNegativeClicked = onNegativeClicked
        )
        interaction(it)
    }
}

@Composable
fun AppModalBottomSheetState.ConnectivityErrorContent(
    onPositiveClicked: (() -> Unit)? = null,
    onNegativeClicked: (() -> Unit)? = null,
    confirmStateChange: ((ModalBottomSheetValue) -> Boolean)? = null,
    interaction: @Composable (ModalBottomSheetState) -> Unit
) {
    val context = LocalContext.current
    warningContent(
        title = context.getString(R.string.connectivity_error_title),
        message = context.getString(R.string.connectivity_error_message),
        positiveButtonLabel = onPositiveClicked?.let {
            context.getString(R.string.connectivity_error_positive_button)
        },
        onPositiveClicked = onPositiveClicked,
        negativeButtonLabel = onNegativeClicked?.let {
            context.getString(R.string.connectivity_error_negative_button)
        },
        onNegativeClicked = onNegativeClicked,
        confirmStateChange = confirmStateChange,
        interaction = interaction
    )
}

fun AppModalBottomSheetState.warningContent(
    title: String,
    message: String,
    positiveButtonLabel: String? = null,
    onPositiveClicked: (() -> Unit)? = null,
    negativeButtonLabel: String? = null,
    onNegativeClicked: (() -> Unit)? = null,
    confirmStateChange: ((ModalBottomSheetValue) -> Boolean)? = null,
    interaction: @Composable (ModalBottomSheetState) -> Unit
) {
    sheetContent(stateChange = confirmStateChange) {
        SheetContent(
            type = ModalBottomSheetAlertType.Warning,
            title = title,
            message = message,
            positiveButtonLabel = positiveButtonLabel,
            onPositiveClicked = onPositiveClicked,
            negativeButtonLabel = negativeButtonLabel,
            onNegativeClicked = onNegativeClicked
        )
        interaction(it)
    }
}

fun AppModalBottomSheetState.infoContent(
    title: String,
    message: String,
    positiveButtonLabel: String? = null,
    onPositiveClicked: (() -> Unit)? = null,
    negativeButtonLabel: String? = null,
    onNegativeClicked: (() -> Unit)? = null,
    confirmStateChange: ((ModalBottomSheetValue) -> Boolean)? = null,
    interaction: @Composable (ModalBottomSheetState) -> Unit
) {
    sheetContent(stateChange = confirmStateChange) {
        SheetContent(
            type = ModalBottomSheetAlertType.Info,
            title = title,
            message = message,
            positiveButtonLabel = positiveButtonLabel,
            onPositiveClicked = onPositiveClicked,
            negativeButtonLabel = negativeButtonLabel,
            onNegativeClicked = onNegativeClicked
        )
        interaction(it)
    }
}

fun AppModalBottomSheetState.questionContent(
    title: String,
    message: String,
    positiveButtonLabel: String? = null,
    onPositiveClicked: (() -> Unit)? = null,
    negativeButtonLabel: String? = null,
    onNegativeClicked: (() -> Unit)? = null,
    confirmStateChange: ((ModalBottomSheetValue) -> Boolean)? = null,
    interaction: @Composable (ModalBottomSheetState) -> Unit
) {
    sheetContent(stateChange = confirmStateChange) {
        SheetContent(
            type = ModalBottomSheetAlertType.Question,
            title = title,
            message = message,
            positiveButtonLabel = positiveButtonLabel,
            onPositiveClicked = onPositiveClicked,
            negativeButtonLabel = negativeButtonLabel,
            onNegativeClicked = onNegativeClicked
        )
        interaction(it)
    }
}

private enum class ModalBottomSheetAlertType {
    Warning, Error, Info, Question
}

@Composable
private fun SheetContent(
    type: ModalBottomSheetAlertType,
    title: String,
    message: String,
    positiveButtonLabel: String? = null,
    onPositiveClicked: (() -> Unit)? = null,
    negativeButtonLabel: String? = null,
    onNegativeClicked: (() -> Unit)? = null,
) {
    val configs = object {
        val color = when (type) {
            ModalBottomSheetAlertType.Warning -> AppTheme.colors.alertWarning
            ModalBottomSheetAlertType.Error -> AppTheme.colors.alertError
            ModalBottomSheetAlertType.Info -> AppTheme.colors.alertInfo
            ModalBottomSheetAlertType.Question -> AppTheme.colors.alertQuestion
        }
        val icon = when (type) {
            ModalBottomSheetAlertType.Warning -> Icons.Default.Warning
            ModalBottomSheetAlertType.Error -> Icons.Default.Close
            ModalBottomSheetAlertType.Info -> Icons.Default.Info
            ModalBottomSheetAlertType.Question -> Icons.Default.QuestionAnswer
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xxnormal))
        Icon(
            imageVector = configs.icon,
            contentDescription = null,
            modifier = Modifier
                .requiredSize(size = AppTheme.Dimens.sizing.xnormal)
        )
        Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xnormal))
        Text(text = title, style = AppTheme.typography.material.h5)
        Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.normal))
        Surface(
            color = if (AppTheme.colors.material.isLight) {
                Color.DarkGray.copy(alpha = .1f)
            } else {
                Color.LightGray.copy(alpha = .1f)
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.Dimens.spacing.normal),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(
                    modifier = Modifier
                        .clip(shape = AppTheme.shapes.material.medium)
                        .fillMaxWidth()
                        .height(height = AppTheme.Dimens.spacing.tiny)
                        .background(color = configs.color)
                )
                Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xxnormal))
                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    style = AppTheme.typography.material.subtitle1
                )
                Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xxnormal))
                if (positiveButtonLabel != null) {
                    AppRoundedCornerButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = positiveButtonLabel,
                        onClick = { onPositiveClicked?.invoke() },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = AppTheme.colors.material.primary
                        ),
                    )
                }
                if (negativeButtonLabel != null) {
                    AppRoundedCornerOutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = negativeButtonLabel,
                        onClick = { onNegativeClicked?.invoke() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = AppTheme.colors.material.surface.copy(
                                alpha = 0f
                            )
                        ),
                    )
                }
                Spacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .height(height = AppTheme.Dimens.spacing.xxnormal)
                )
            }
        }
    }
}