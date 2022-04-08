package com.sicredi.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import kotlin.math.roundToInt

internal class AppCollapsingTopBarState(
    private val minHeight: Int,
    private val enableTopBarScroll: Boolean = false
) {
    var maxHeight: Int
        get() = mMaxHeight
        set(height) {
            mMaxHeight = height
            heightOffset = heightOffset.coerceIn(
                minimumValue = minHeightOffsetPx.toFloat(),
                maximumValue = 0f
            )
        }
    var mMaxHeight by mutableStateOf(0)
        private set
    var heightOffset by mutableStateOf(0f)
        private set
    var yOffset by mutableStateOf(0f)
        private set
    val transition: Float
        get() = ((mMaxHeight - minHeight) + heightOffset) / (mMaxHeight - minHeight)
    val height: Int
        get() = mMaxHeight + heightOffset.roundToInt()

    private val minHeightOffsetPx: Int
        get() = -mMaxHeight + minHeight
    private val minYOffsetPx: Int
        get() = -minHeight

    val nestedScrollConnection: NestedScrollConnection = ScrollConnection()

    private inner class ScrollConnection : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val delta = available.y

            val minHeightOffset = minHeightOffsetPx.toFloat()
            val minYOffset = minYOffsetPx.toFloat()

            val totalOffset = heightOffset + yOffset

            val newHeightOffset = totalOffset + delta
            heightOffset = newHeightOffset.coerceIn(
                minimumValue = minHeightOffset,
                maximumValue = 0f
            )
            if (enableTopBarScroll) {
                val newOffset = totalOffset +
                        mMaxHeight - minHeight + delta

                yOffset = newOffset.coerceIn(
                    minimumValue = minYOffset,
                    maximumValue = 0f
                )
            }
            val isInMaxState = heightOffset == 0f
            val isInMinState = if (enableTopBarScroll) {
                yOffset == -minHeight.toFloat()
            } else {
                heightOffset == minHeightOffset
            }
            return if (isInMaxState || isInMinState) {
                Offset.Zero
            } else {
                available
            }
        }
    }

    companion object {
        fun saver(
            minHeight: Int, enableTopBarScroll: Boolean = false
        ): Saver<AppCollapsingTopBarState, *> = Saver(
            save = {
                arrayListOf(
                    it.mMaxHeight,
                    it.heightOffset,
                    it.yOffset
                )
            },
            restore = {
                AppCollapsingTopBarState(
                    minHeight = minHeight,
                    enableTopBarScroll = enableTopBarScroll
                ).apply {
                    maxHeight = it[0] as Int
                    heightOffset = it[1] as Float
                    yOffset = it[2] as Float
                }
            }
        )
    }
}

@Composable
internal fun rememberAppCollapsingTopBarState(minHeight: Int, enableTopBarScroll: Boolean = false) =
    remember {
        AppCollapsingTopBarState(minHeight = minHeight, enableTopBarScroll = enableTopBarScroll)
    }

@Composable
internal fun rememberSaveableAppCollapsingTopBarState(
    minHeight: Int, enableTopBarScroll: Boolean = false
) = rememberSaveable(
    saver = AppCollapsingTopBarState.saver(
        minHeight = minHeight, enableTopBarScroll = enableTopBarScroll
    )
) {
    AppCollapsingTopBarState(minHeight = minHeight, enableTopBarScroll = enableTopBarScroll)
}