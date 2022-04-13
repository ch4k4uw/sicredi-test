package com.sicredi.domain.checkin.infra.repository

import com.sicredi.core.data.AppDispatchers
import com.sicredi.domain.AppInstantTaskExecutorRule
import com.sicredi.domain.MockAppDispatchers
import com.sicredi.domain.checkin.domain.data.AppNoLoggedUserException
import com.sicredi.domain.checkin.domain.repository.CheckInCmdRepository
import com.sicredi.domain.checkin.infra.remote.CheckInApi
import com.sicredi.domain.checkin.infra.repository.stuff.CheckInFixture
import com.sicredi.domain.common.stuff.CommonFixture
import com.sicredi.domain.credential.domain.entity.User
import com.sicredi.domain.credential.domain.repository.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CheckInCmdRepositoryTest {
    @get:Rule
    val testRule = AppInstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var checkInApi: CheckInApi

    @RelaxedMockK
    private lateinit var userRepository: UserRepository

    private lateinit var repository: CheckInCmdRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = CheckInCmdRepositoryImpl(
            checkInApi = checkInApi,
            userRepository = userRepository,
            appDispatchers = MockAppDispatchers()
        )
    }

    @Test
    fun `it should perform check-in`() {
        checkInApi.setup()
        userRepository.setup()

        runBlocking {
            repository.performCheckIn(eventId = CheckInFixture.CheckInRequest.eventId).first()
        }

        coVerify(exactly = 1) {
            checkInApi.performCheckIn(event = CheckInFixture.CheckInRequest)
        }
    }

    @Test(expected = AppNoLoggedUserException::class)
    fun `it shouldn't perform check-in (no logged user)`() {
        checkInApi.setup()
        userRepository.setup(hasLoggedUser = false)

        runBlocking {
            repository.performCheckIn(eventId = CheckInFixture.CheckInRequest.eventId).first()
        }
    }

    private fun CheckInApi.setup() {
        coEvery { performCheckIn(event = any()) } returns Unit
    }

    private fun UserRepository.setup(hasLoggedUser: Boolean = true) {
        coEvery { findLogged() } returns flowOf(
            if (hasLoggedUser) CommonFixture.User else User.Empty
        )
    }
}