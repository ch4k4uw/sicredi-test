package com.sicredi.core.ui.compose.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.sicredi.core.extensions.measureFirst
import com.sicredi.core.extensions.toPx
import com.sicredi.core.ui.compose.AppTheme
import kotlin.math.roundToInt

@Composable
fun AppCollapsingTopBarScaffold(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppTheme.colors.material.primarySurface,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    fab: @Composable (() -> Unit)? = null,
    fixedTopBar: Boolean = fab != null,
    saveStateEnabled: Boolean = false,
    barContentHeight: Dp,
    barContent: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val barAttrs = object {
        val height = AppCollapsingTopBarDefaults.BarHeight
        val heightPx = height.toPx().roundToInt()
        val enableScroll = !fixedTopBar
        val contentPadding = AppCollapsingTopBarDefaults.BarHorizontalPadding
        val contentPaddingPx = AppCollapsingTopBarDefaults.BarHorizontalPadding.toPx()
        val fabSpacing = AppTheme.Dimens.spacing.normal
        val elevation = AppBarDefaults.TopAppBarElevation
        val iconWidth = AppCollapsingTopBarDefaults.TitleIconWidth
        val iconInsetWidth = AppCollapsingTopBarDefaults.IconInsetWidth
    }
    val topBarState = if (saveStateEnabled) {
        rememberSaveableAppCollapsingTopBarState(
            minHeight = barAttrs.heightPx,
            enableTopBarScroll = barAttrs.enableScroll
        )
    } else {
        rememberAppCollapsingTopBarState(
            minHeight = barAttrs.heightPx,
            enableTopBarScroll = barAttrs.enableScroll
        )
    }

    topBarState.maxHeight = barContentHeight.toPx().roundToInt()

    CompositionLocalProvider(
        AppCollapsingTopBar.LocalBackgroundColor provides backgroundColor
    ) {
        SubcomposeLayout(
            modifier = modifier.then(
                other = Modifier.nestedScroll(connection = topBarState.nestedScrollConnection)
            )
        ) { constraints ->
            val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

            val topBarId = AppCollapsingTopBar.LayoutId.TopBar
            val navButtonId = AppCollapsingTopBar.LayoutId.NavButton
            val actionsId = AppCollapsingTopBar.LayoutId.Actions
            val titleId = AppCollapsingTopBar.LayoutId.Title
            val contentId = AppCollapsingTopBar.LayoutId.Content
            val fabId = AppCollapsingTopBar.LayoutId.Fab

            val collapsingTransition = (1.7f * topBarState.transition).coerceIn(0f, 1f)
            val collapsingAlphaTransition = (3.0f * topBarState.transition).coerceIn(0f, 1f)

            val topBarPlaceable = subcompose(slotId = topBarId) {
                AppCollapsingTopBarTopBar(
                    modifier = Modifier.alpha(alpha = collapsingAlphaTransition),
                    elevation = barAttrs.elevation,
                    height = topBarState.height.toDp(),
                    content = barContent
                )
            }.measureFirst(constraints = looseConstraints)

            val navButtonPlaceable = subcompose(slotId = navButtonId) {
                AppCollapsingTopBarTopBarHeader(
                    horizontalArrangement = Arrangement.Start,
                    height = barAttrs.height,
                    startPadding = barAttrs.contentPadding
                ) {
                    AppCollapsingTopBarNavButton(
                        navigationIcon = navigationIcon,
                        width = navigationIcon?.run { barAttrs.iconWidth - barAttrs.iconInsetWidth }
                            ?: barAttrs.iconWidth - barAttrs.contentPadding
                    )
                }
            }.measureFirst(constraints = looseConstraints)

            val actionsPlaceable = subcompose(slotId = actionsId) {
                AppCollapsingTopBarTopBarHeader(
                    horizontalArrangement = Arrangement.End,
                    height = barAttrs.height,
                    endPadding = barAttrs.contentPadding,
                    content = { AppCollapsingTopBarActions(actions = actions) }
                )
            }.measureFirst(constraints = looseConstraints)

            val titlePlaceable = subcompose(slotId = titleId) {
                AppCollapsingTopBarTitle(
                    height = barAttrs.height, fontSizeTransition = collapsingTransition, title = title
                )
            }.measureFirst(
                constraints = looseConstraints.copy(
                    maxWidth = topBarPlaceable.width - (
                            lerp(
                                start = Dp(
                                    value = (navButtonPlaceable.width + actionsPlaceable.width)
                                        .toFloat()
                                ),
                                stop = Dp(value = barAttrs.contentPaddingPx * 2f),
                                fraction = collapsingTransition
                            )
                            ).value.roundToInt()
                )
            )

            val contentPlaceable = subcompose(slotId = contentId) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    content = content
                )
            }.measureFirst(constraints = looseConstraints)

            val fabPlaceable = fab?.let { fabContent ->
                subcompose(slotId = fabId) {
                    AppCollapsingTopBarFabButton(
                        paddingEnd = barAttrs.fabSpacing,
                        collapsingTransition = collapsingTransition,
                        collapsingAlphaTransition = collapsingAlphaTransition,
                        content = fabContent
                    )
                }.measureFirst(constraints = looseConstraints)
            }

            val maxHeight = maxOf(topBarPlaceable.height, contentPlaceable.height)

            layout(width = topBarPlaceable.width, height = maxHeight) {
                val baseY = topBarState.yOffset.roundToInt()
                topBarPlaceable.placeRelative(x = 0, y = baseY)
                contentPlaceable.placeRelative(x = 0, y = baseY + topBarPlaceable.height)
                navButtonPlaceable.placeRelative(x = 0, y = baseY)
                actionsPlaceable.placeRelative(
                    x = topBarPlaceable.width - actionsPlaceable.width, y = baseY
                )
                val topBarContentPadding = barAttrs.contentPaddingPx.roundToInt()
                val maxTitleWidth =
                    titlePlaceable.width + topBarContentPadding * 2
                val titleStopStartPadding = (topBarPlaceable.width - (maxTitleWidth))
                    .coerceIn(
                        topBarContentPadding..topBarPlaceable.width
                    )
                val titleStartPadding = lerp(
                    start = Dp(value = navButtonPlaceable.width.toFloat()),
                    stop = Dp(value = titleStopStartPadding.toFloat()),
                    fraction = collapsingTransition
                ).value.roundToInt()
                val titleBottomPadding = lerp(
                    start = Dp(value = 0f),
                    stop = barAttrs.contentPadding,
                    fraction = collapsingTransition
                ).value.roundToInt()
                titlePlaceable.placeRelative(
                    x = titleStartPadding.let {
                        val maxX = topBarPlaceable.width - topBarContentPadding
                        val end = it + titlePlaceable.width
                        if (end > maxX) {
                             -end + maxX + it
                        } else {
                            it
                        }
                    },
                    y = topBarPlaceable.height - titlePlaceable.height + baseY - titleBottomPadding
                )
                fabPlaceable?.placeRelative(
                    x = topBarPlaceable.width - fabPlaceable.width,
                    y = topBarPlaceable.height - fabPlaceable.height / 2
                )
            }
        }
    }
}

private object AppCollapsingTopBar {
    enum class LayoutId {
        TopBar, NavButton, Title, Actions, Content, Fab
    }

    val LocalBackgroundColor = compositionLocalOf { Color.Black }
}

@Composable
private fun AppCollapsingTopBarTopBar(
    modifier: Modifier,
    elevation: Dp,
    height: Dp,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = AppCollapsingTopBar.LocalBackgroundColor.current,
        elevation = elevation,
        contentColor = contentColorFor(
            backgroundColor = AppCollapsingTopBar.LocalBackgroundColor.current
        )
    ) {
        Box(
            modifier = Modifier
                .height(height = height)
                .then(other = modifier),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}

@Composable
private fun AppCollapsingTopBarTopBarHeader(
    horizontalArrangement: Arrangement.Horizontal,
    height: Dp,
    startPadding: Dp = 0.dp,
    endPadding: Dp = 0.dp,
    content: @Composable RowScope.() -> Unit
) {
    val contentColor = contentColorFor(
        backgroundColor = AppCollapsingTopBar.LocalBackgroundColor.current
    )
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Row(
            Modifier
                .height(height = height)
                .padding(start = startPadding, end = endPadding),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Composable
private fun AppCollapsingTopBarNavButton(
    navigationIcon: @Composable (() -> Unit)?,
    width: Dp
) {
    if (navigationIcon == null) {
        Spacer(
            Modifier
                .fillMaxHeight()
                .width(width = width)
        )
    } else {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .width(width = width),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(
                LocalContentAlpha provides ContentAlpha.high,
                content = navigationIcon
            )
        }
    }
}

@Composable
private fun AppCollapsingTopBarActions(
    actions: @Composable RowScope.() -> Unit
) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )
    }
}

@Composable
private fun AppCollapsingTopBarTitle(
    height: Dp,
    fontSizeTransition: Float,
    title: @Composable () -> Unit
) {
    @Composable
    fun calculateFontSize(): TextUnit {
        val small = AppTheme.typography.material.h6.fontSize
        val large = AppTheme.typography.material.h4.fontSize
        return lerp(start = small, stop = large, fraction = fontSizeTransition)
    }

    val titleFont = AppTheme.typography.material.h6.copy(
        fontSize = calculateFontSize()
    )
    Row(
        Modifier
            .height(height = height),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val contentColor = contentColorFor(
            backgroundColor = AppCollapsingTopBar.LocalBackgroundColor.current
        )
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            ProvideTextStyle(value = titleFont) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.high,
                    content = title
                )
            }
        }
    }
}

@Composable
private fun AppCollapsingTopBarFabButton(
    paddingEnd: Dp,
    collapsingTransition: Float,
    collapsingAlphaTransition: Float,
    content: @Composable () -> Unit
) {
    val contentColor = contentColorFor(
        backgroundColor = AppCollapsingTopBar.LocalBackgroundColor.current
    )
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Row(
            modifier = Modifier
                .padding(end = paddingEnd)
                .alpha(alpha = collapsingAlphaTransition)
                .scale(
                    scale = lerp(
                        start = Dp(value = .7f),
                        stop = Dp(value = 1f),
                        fraction = collapsingTransition
                    ).value
                )
                .rotate(
                    degrees = lerp(
                        start = Dp(value = 0f),
                        stop = Dp(value = 360f),
                        fraction = collapsingTransition
                    ).value
                ),
            content = { content() }
        )
    }
}

// region preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun AppCollapsingTopBarScaffoldPreviewLight() {
    AppCollapsingTopBarScaffoldPreview()
}

@Composable
private fun AppCollapsingTopBarScaffoldPreview() {
    AppTheme {
        AppBackground {
            BoxWithConstraints {
                AppCollapsingTopBarScaffold(
                    title = {
                        Text(
                            text = "Top bar preview, with long text",
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            softWrap = false
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { }) {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "")
                        }
                    },
                    fab = {
                        FloatingActionButton(onClick = { }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "")
                        }
                    },
                    barContentHeight = maxWidth * .7f,
                    barContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.Red)
                        ) {

                        }
                    }
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
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AppCollapsingTopBarScaffoldPreviewDark() {
    AppCollapsingTopBarScaffoldPreview()
}
// endregion