package com.sicredi.instacredi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.runner.Description

class AppInstantTaskExecutorRule : InstantTaskExecutorRule() {
    private val dispatcher = StandardTestDispatcher()
    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }

    fun advanceUntilIdle() {
        dispatcher.scheduler.advanceUntilIdle()
    }
}