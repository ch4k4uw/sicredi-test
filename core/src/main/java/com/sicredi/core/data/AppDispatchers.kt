package com.sicredi.core.data

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
open class AppDispatchers @Inject constructor() {
    open val io: CoroutineContext = Dispatchers.IO
    open val main: CoroutineContext = Dispatchers.Main
}