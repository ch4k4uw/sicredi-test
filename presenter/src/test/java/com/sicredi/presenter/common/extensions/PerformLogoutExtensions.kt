package com.sicredi.presenter.common.extensions

import com.sicredi.presenter.common.uc.PerformLogout
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf

fun PerformLogout.setup() {
    coEvery { this@setup.invoke() } returns flowOf(Unit)
}