package com.sicredi.presenter.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.yield

abstract class BaseViewModel<State> : ViewModel() {
    private val mutableState = MutableSharedFlow<State>(replay = 0)
    val state: Flow<State> = mutableState
    private var isActivated = false
    init {
        mutableState
            .subscriptionCount
            .map { it > 0 }
            .distinctUntilChanged()
            .onEach { activated ->
                isActivated = activated
            }
            .launchIn(viewModelScope)
    }

    protected suspend infix fun Flow<State>.emit(state: State) {
        while(!isActivated) yield()
        mutableState.emit(value = state)
    }

    protected infix fun Flow<State>.tryEmit(state: State): Boolean =
        mutableState.tryEmit(state)
}