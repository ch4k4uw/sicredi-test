package com.sicredi.core.ui.compose.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sicredi.core.extensions.ConnectivityErrorContent
import com.sicredi.core.extensions.GenericErrorContent
import com.sicredi.core.ui.compose.AppTheme

val LocalAppModalBottomSheetState = staticCompositionLocalOf<AppModalBottomSheetState> {
    TODO("Undefined")
}

private typealias SheetContent = @Composable ColumnScope.(ModalBottomSheetState) -> Unit

@Stable
class AppModalBottomSheetState {
    var sheetContent by mutableStateOf<SheetContent?>(null)
        private set

    var confirmStateChange by mutableStateOf<((ModalBottomSheetValue) -> Boolean)?>(null)
        private set

    fun sheetContent(
        stateChange: ((ModalBottomSheetValue) -> Boolean)? = null,
        content: @Composable ColumnScope.(ModalBottomSheetState) -> Unit,
    ) {
        sheetContent = content
        confirmStateChange = stateChange
    }
}

@Composable
fun rememberAppModalBottomSheetState() =
    remember { AppModalBottomSheetState() }

@Composable
fun AppModalBottomSheetHost(
    modifier: Modifier = Modifier,
    state: AppModalBottomSheetState = LocalAppModalBottomSheetState.current,
    content: @Composable () -> Unit = {}
) {

    val confirmStateChange: (ModalBottomSheetValue) -> Boolean = {
        state.confirmStateChange?.invoke(it) ?: it != ModalBottomSheetValue.Hidden
    }

    val modalState = rememberSaveable(
        saver = ModalBottomSheetState.Saver(
            animationSpec = SwipeableDefaults.AnimationSpec,
            confirmStateChange = confirmStateChange
        )
    ) {
        ModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = confirmStateChange
        )
    }
    AppModalBottomSheetLayout(
        modifier = modifier, state = state, modalState = modalState, content = content
    )
}

@Composable
private fun AppModalBottomSheetLayout(
    modifier: Modifier = Modifier,
    state: AppModalBottomSheetState,
    modalState: ModalBottomSheetState,
    content: @Composable () -> Unit = {}
) {
    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = modalState,
        sheetContent = {
            val sheetContent = state.sheetContent
            if (sheetContent != null) {
                sheetContent(modalState)
            } else {
                Text(text = "No content")
            }
        },
        sheetShape = AppTheme.shapes.bottomSheetDialog,
        content = content
    )
}

@Preview(showSystemUi = true)
@Composable
fun PreviewAppModalBottomSheetLayout() {
    AppTheme {
        var showBottomSheet1 by remember { mutableStateOf(false) }
        var showBottomSheet2 by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { showBottomSheet1 = true }) {
                Text(text = "Show bottom sheet")
            }
        }

        if (showBottomSheet1) {
            var show2 by remember { mutableStateOf(false) }
            LocalAppModalBottomSheetState.current.GenericErrorContent(
                onPositiveClicked = {
                    showBottomSheet1 = false
                },
                onNegativeClicked = {
                    showBottomSheet1 = false
                    show2 = true
                },
            ) {
                LaunchedEffect(showBottomSheet1, show2) {
                    if (showBottomSheet1) {
                        it.show()
                    } else {
                        it.hide()
                        if (show2) {
                            showBottomSheet2 = true
                        }
                    }
                }
            }
            /*LocalAppModalBottomSheetState.current.sheetContent {
                var show2 by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = { showBottomSheet1 = false }) {
                            Text(text = "1: Close bottom sheet")
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(onClick = {
                            showBottomSheet1 = false
                            show2 = true
                        }) {
                            Text(text = "1: Show bottom sheet 2")
                        }
                    }
                }
                LaunchedEffect(showBottomSheet1, show2) {
                    if (showBottomSheet1) {
                        it.show()
                    } else {
                        it.hide()
                        if (show2) {
                            showBottomSheet2 = true
                        }
                    }
                }
            }*/
        }

        if (showBottomSheet2) {
            LocalAppModalBottomSheetState.current.ConnectivityErrorContent(
                onPositiveClicked = {
                    showBottomSheet2 = false
                },
                onNegativeClicked = {
                    showBottomSheet2 = false
                },
            ) {
                LaunchedEffect(showBottomSheet2) {
                    if (showBottomSheet2) {
                        it.show()
                    } else {
                        it.hide()
                    }
                }
            }
            /*LocalAppModalBottomSheetState.current.sheetContent {
                Box(
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = { showBottomSheet2 = false }) {
                        Text(text = "2: Close bottom sheet")
                    }
                }
                LaunchedEffect(showBottomSheet2) {
                    if (showBottomSheet2) {
                        it.show()
                    } else {
                        it.hide()
                    }
                }
            }*/

        }
    }
}