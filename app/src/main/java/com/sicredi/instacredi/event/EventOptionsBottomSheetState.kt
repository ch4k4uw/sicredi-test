package com.sicredi.instacredi.event

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Beenhere
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.sicredi.core.ui.compose.component.AppBackground
import com.sicredi.core.ui.compose.AppTheme
import com.sicredi.core.ui.compose.component.AppModalBottomSheetState
import com.sicredi.instacredi.R

@Composable
fun AppModalBottomSheetState.EventOptions(
    onCheckInClick: () -> Unit,
    onShareClick: () -> Unit,
    onGoogleMapsClick: () -> Unit,
    stateChange: (ModalBottomSheetValue) -> Boolean,
    interaction: @Composable (ModalBottomSheetState) -> Unit
) {
    sheetContent(stateChange = stateChange) {
        EventOptionsContent(
            onCheckInClick = onCheckInClick,
            onShareClick = onShareClick,
            onGoogleMapsClick = onGoogleMapsClick
        )
        interaction(it)
    }
}

@Composable
private fun EventOptionsContent(
    onCheckInClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onGoogleMapsClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
    ) {
        EventOptionsButton(
            onClick = onCheckInClick,
            text = R.string.event_options_check_in_action,
            icon = Icons.Default.Beenhere,
            addTopSpacer = true
        )
        EventOptionsButton(
            onClick = onShareClick,
            text = R.string.event_options_share_action,
            icon = Icons.Default.Share
        )
        EventOptionsButton(
            onClick = onGoogleMapsClick,
            text = R.string.event_options_maps_action,
            icon = Icons.Default.LocationOn,
            addBottomSpacer = true
        )
    }
}

@Composable
private fun EventOptionsButton(
    onClick: () -> Unit,
    @StringRes text: Int,
    icon: ImageVector,
    addTopSpacer: Boolean = false,
    addBottomSpacer: Boolean = false
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color.Transparent,
        contentColor = AppTheme.colors.material.onBackground,
        onClick = onClick,
        role = Role.Button,
    ) {
        Column(modifier = Modifier.padding(start = AppTheme.Dimens.spacing.normal)) {
            val topSpacerSize = if (!addTopSpacer) {
                AppTheme.Dimens.spacing.normal
            } else {
                AppTheme.Dimens.spacing.xxnormal
            }
            Spacer(modifier = Modifier.height(height = topSpacerSize))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null)
                Spacer(modifier = Modifier.width(width = AppTheme.Dimens.spacing.small))
                Text(text = stringResource(id = text), style = AppTheme.typography.material.button)
            }
            val bottomSpacerSize = if (!addBottomSpacer) {
                AppTheme.Dimens.spacing.normal
            } else {
                AppTheme.Dimens.spacing.large
            }
            Spacer(modifier = Modifier.height(height = bottomSpacerSize))
        }
    }
}

// region Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun EventOptionsContentPreviewLight() {
    AppTheme {
        AppBackground {
            EventOptionsContentPreview()
        }
    }
}

@Composable
private fun EventOptionsContentPreview() {
    EventOptionsContent()
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EventOptionsContentPreviewDark() {
    AppTheme {
        AppBackground {
            EventOptionsContentPreview()
        }
    }
}
// endregion