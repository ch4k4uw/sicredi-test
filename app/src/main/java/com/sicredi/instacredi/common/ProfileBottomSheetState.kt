package com.sicredi.instacredi.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.sicredi.core.extensions.AppBackground
import com.sicredi.core.ui.compose.AppTheme
import com.sicredi.core.ui.compose.component.AppModalBottomSheetState
import com.sicredi.instacredi.R

@Composable
fun AppModalBottomSheetState.ProfileBottomSheet(
    name: String,
    email: String,
    onLogoutClick: () -> Unit,
    confirmStateChange: ((ModalBottomSheetValue) -> Boolean)? = null,
    interaction: @Composable (ModalBottomSheetState) -> Unit
) {
    sheetContent(stateChange = confirmStateChange) {
        ProfileBottomSheetContent(name = name, email = email, onLogoutClick = onLogoutClick)
        interaction(it)
    }
}

@Composable
private fun ProfileBottomSheetContent(
    name: String = "", email: String = "", onLogoutClick: () -> Unit = {}
) {
    Row {
        SheetHorizontalMargin()
        Column(modifier = Modifier.weight(weight = 1f, fill = false)) {
            SheetTitle()
            SheetBody(name = name, email = email)
            SheetLogoutButton(onClick = onLogoutClick)
        }
        SheetHorizontalMargin()
    }
}

@Composable
private fun SheetHorizontalMargin() {
    Spacer(modifier = Modifier.width(width = AppTheme.Dimens.spacing.normal))
}

@Composable
private fun SheetTitle() {
    Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xtiny))
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(id = R.string.app_name),
        textAlign = TextAlign.Center,
        style = AppTheme.typography.material.h5
    )
}

@Composable
private fun SheetBody(name: String, email: String) {
    Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.large))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = Icons.Default.Person, contentDescription = null)
        Spacer(modifier = Modifier.width(width = AppTheme.Dimens.spacing.small))
        Column {
            Text(text = name, style = AppTheme.typography.material.body2)
            Text(text = email, style = AppTheme.typography.material.caption)
        }
    }
}

@Composable
private fun SheetLogoutButton(onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.normal))
    Button(modifier = Modifier.fillMaxWidth(fraction = .85f), onClick = onClick) {
        Text(text = stringResource(id = R.string.logout_action))
    }
    Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xxnormal))
}

// region preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ProfileBottomSheetContentPreviewLight() {
    ProfileBottomSheetContentPreview()
}

@Composable
private fun ProfileBottomSheetContentPreview() {
    AppTheme {
        AppBackground {
            ProfileBottomSheetContent(name = "Pedro Motta", email = "pedro.motta@avenuecode.com")
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProfileBottomSheetContentPreviewDark() {
    ProfileBottomSheetContentPreview()
}

// endregion