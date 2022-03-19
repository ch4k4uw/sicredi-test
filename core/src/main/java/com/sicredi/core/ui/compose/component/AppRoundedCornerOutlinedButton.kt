package com.sicredi.core.ui.compose.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppRoundedCornerOutlinedButton(
    modifier: Modifier = Modifier,
    label: String? = null,
    onClick: () -> Unit = {},
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    content: @Composable RowScope.() -> Unit = {},
) {
    OutlinedButton(
        modifier = modifier,
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