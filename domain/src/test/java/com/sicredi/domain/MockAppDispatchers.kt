package com.sicredi.domain

import com.sicredi.core.data.AppDispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class MockAppDispatchers : AppDispatchers() {
    override val io: CoroutineContext
        get() = EmptyCoroutineContext
    override val main: CoroutineContext
        get() = EmptyCoroutineContext
}