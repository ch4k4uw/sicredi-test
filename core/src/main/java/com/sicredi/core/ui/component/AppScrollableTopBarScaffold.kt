package com.sicredi.core.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import com.sicredi.core.extensions.AppBackground
import com.sicredi.core.ui.compose.AppTheme

private object AppScrollableTopBarDefaults {
    enum class LayoutId {
        TopBar, Content
    }
}

@Composable
fun AppScrollableTopBarScaffold(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    saveScrollStateEnabled: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollConnection = if (!saveScrollStateEnabled) {
        rememberAppScrollableTopBarScrollConnection()
    } else {
        rememberSaveableAppScrollableTopBarScrollConnection()
    }

    SubcomposeLayout(
        modifier = modifier.then(other = Modifier.nestedScroll(connection = scrollConnection))
    ) { constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val topBarId = AppScrollableTopBarDefaults.LayoutId.TopBar
        val contentId = AppScrollableTopBarDefaults.LayoutId.Content

        val topBarPlaceable = subcompose(slotId = topBarId) {
            TopAppBar(title = title, navigationIcon = navigationIcon, actions = actions)
        }[0].measure(constraints = looseConstraints)

        scrollConnection.topBarHeightPx = topBarPlaceable.height

        val contentPlaceable = subcompose(slotId = contentId) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                content = content
            )
        }[0].measure(constraints = looseConstraints)

        val maxHeight = maxOf(topBarPlaceable.height, contentPlaceable.height)

        layout(width = topBarPlaceable.width, height = maxHeight) {
            val baseY = scrollConnection.topBarYOffsetPx.toInt()
            topBarPlaceable.placeRelative(x = 0, y = baseY)
            contentPlaceable.placeRelative(x = 0, y = baseY + topBarPlaceable.height)
        }
    }
}

@Composable
private fun rememberAppScrollableTopBarScrollConnection() =
    remember { AppScrollableTopBarScrollConnectionImpl() }

private class AppScrollableTopBarScrollConnectionImpl : NestedScrollConnection {
    var topBarHeightPx by mutableStateOf(0)
    var topBarYOffsetPx by mutableStateOf(0f)
        private set

    private val isConsumed: Boolean
        get() = topBarYOffsetPx == 0f || topBarYOffsetPx == -topBarHeightPx.toFloat()

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val delta = available.y
        val newTopBarYOffset = topBarYOffsetPx + delta
        topBarYOffsetPx = newTopBarYOffset
            .coerceIn(minimumValue = -topBarHeightPx.toFloat(), maximumValue = 0f)
        return if (isConsumed) {
            Offset.Zero
        } else {
            available
        }
    }

    companion object {
        val SAVER: Saver<AppScrollableTopBarScrollConnectionImpl, *> = Saver(
            save = { bundleOf("hYOffPx" to it.topBarYOffsetPx) },
            restore = {
                AppScrollableTopBarScrollConnectionImpl().apply {
                    topBarYOffsetPx = it.getFloat("hYOffPx")
                }
            }
        )
    }
}

@Composable
private fun rememberSaveableAppScrollableTopBarScrollConnection() =
    rememberSaveable(saver = AppScrollableTopBarScrollConnectionImpl.SAVER) {
        AppScrollableTopBarScrollConnectionImpl()
    }

// region preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun AppScrollableTopBarPreviewLight() {
    AppScrollableTopBarPreview()
}

@Composable
private fun AppScrollableTopBarPreview() {
    AppTheme {
        AppBackground {
            AppScrollableTopBarScaffold(
                title = { Text(text = "Top bar preview") }
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        Text(text = "Scrollable top bar preview content")
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AppScrollableTopBarPreviewDark() {
    AppScrollableTopBarPreview()
}

// endregion