package com.sicredi.domain.feed.infra.repository

import com.sicredi.core.network.domain.data.AppHttpException
import com.sicredi.core.network.domain.data.AppHttpGenericException
import com.sicredi.domain.MockAppDispatchers
import com.sicredi.domain.feed.domain.repository.EventRepository
import com.sicredi.domain.feed.infra.remote.EventApi
import com.sicredi.domain.feed.infra.repository.stuff.EventApiFixture
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EventRepositoryTest {
    @RelaxedMockK
    private lateinit var eventApi: EventApi

    private lateinit var repository: EventRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = EventRepositoryImpl(eventApi, MockAppDispatchers())
    }

    @Test
    fun `it should fetch and parse all events`() {
        eventApi.setup()
        runBlocking {
            repository.findAll()
                .collect { events ->
                    assertEquals(EventApiFixture.AllEvents.size, events.size)
                    events.forEach { event ->
                        assertEquals(EventApiFixture.EventDefaultDate, event.date)
                    }
                }
        }
        coVerify(exactly = 1) { eventApi.findAll() }
        coVerify(exactly = 0) { eventApi.find(any()) }
    }

    @Test
    fun `it shouldn't fetch events`() {
        eventApi.setupException()
        runBlocking {
            repository.findAll()
                .catch {
                    assertTrue(
                        "must be a ${AppHttpException::class.java.simpleName} error",
                        it is AppHttpException
                    )
                }
                .collect {
                    assertFalse("it must fail", true)
                }
        }
        coVerify(exactly = 1) { eventApi.findAll() }
        coVerify(exactly = 0) { eventApi.find(any()) }
    }

    @Test
    fun `it should fetch and parse an event`() {
        eventApi.setup()
        runBlocking {
            repository.find("xxx")
                .collect { event ->
                    assertEquals(EventApiFixture.EventDefaultDate, event.date)
                }
        }
        coVerify(exactly = 0) { eventApi.findAll() }
        coVerify(exactly = 1) { eventApi.find(any()) }
    }

    @Test
    fun `it shouldn't fetch the event`() {
        eventApi.setupException()
        runBlocking {
            repository.find("xxx")
                .catch {
                    assertTrue(
                        "must be a ${AppHttpException::class.java.simpleName} error",
                        it is AppHttpException
                    )
                }
                .collect {
                    assertFalse("it must fail", true)
                }
        }
        coVerify(exactly = 0) { eventApi.findAll() }
        coVerify(exactly = 1) { eventApi.find(any()) }
    }

    private fun EventApi.setup() {
        coEvery { findAll() } returns EventApiFixture.AllEvents
        coEvery { find(any()) } returns EventApiFixture.AnEvent
    }

    private fun EventApi.setupException(
        exception: Throwable = AppHttpGenericException(code = 501)
    ) {
        coEvery { findAll() } throws exception
        coEvery { find(any()) } throws exception
    }
}