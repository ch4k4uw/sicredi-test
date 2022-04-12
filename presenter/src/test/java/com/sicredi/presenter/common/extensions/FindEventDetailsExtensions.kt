package com.sicredi.presenter.common.extensions

import com.sicredi.presenter.common.uc.FindEventDetails
import com.sicredi.presenter.feed.stuff.EventsFixture
import io.mockk.coEvery
import kotlinx.coroutines.flow.flow

fun FindEventDetails.setup(exception: Throwable? = null) {
    coEvery { this@setup.invoke(any()) } returns flow {
        exception?.run { throw this } ?: emit(EventsFixture.AnEvent)
    }
}