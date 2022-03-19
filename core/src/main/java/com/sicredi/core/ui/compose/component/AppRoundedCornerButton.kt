package com.sicredi.core.ui.compose.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppRoundedCornerButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    onClick: () -> Unit = {},
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    content: @Composable RowScope.() -> Unit = {},
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(percent = 50),
        onClick = onClick,
        colors = colors,
        content = {
            if (label != null) {
                Text(text = label)
            } else {
                content()
            }
        }
    )
}