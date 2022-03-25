package com.sicredi.instacredi.common.extensions

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import com.sicredi.presenter.common.BaseViewModel

internal fun <T : Saver<Original, Saveable>, Original, Saveable> save(
    value: Original,
    saver: T,
    scope: SaverScope
): Any {
    return with(saver) { scope.save(value) } ?: false
}

internal inline fun <T : Saver<Original, Saveable>, Original, Saveable, reified Result> restore(
    value: Saveable?,
    saver: T
): Result {
    return value?.let { with(saver) { restore(value) } as Result }!!
}

@NonRestartableComposable
@Composable
internal fun <ViewModel, State> ViewModelEventHandlingEffect(
    viewModel: ViewModel, context: Context, handler: (State) -> Unit
) where ViewModel : BaseViewModel<State> {
    LaunchedEffect(context) {
        viewModel.state.collect(handler)
    }
}