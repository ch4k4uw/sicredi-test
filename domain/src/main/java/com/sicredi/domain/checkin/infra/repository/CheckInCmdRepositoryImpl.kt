package com.sicredi.domain.checkin.infra.repository

import com.sicredi.core.data.AppDispatchers
import com.sicredi.domain.checkin.domain.data.AppNoLoggedUserException
import com.sicredi.domain.checkin.domain.repository.CheckInCmdRepository
import com.sicredi.domain.checkin.infra.remote.CheckInApi
import com.sicredi.domain.checkin.infra.remote.model.CheckInRequest
import com.sicredi.domain.credential.domain.entity.User
import com.sicredi.domain.credential.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class CheckInCmdRepositoryImpl @Inject constructor(
    private val checkInApi: CheckInApi,
    private val userRepository: UserRepository,
    private val appDispatchers: AppDispatchers
) : CheckInCmdRepository {
    override suspend fun performCheckIn(eventId: String): Flow<Unit> = flow {
        val user = userRepository
            .findLogged()
            .single()
        assertValidUser(user = user)

        checkInApi
            .performCheckIn(
                CheckInRequest(
                    eventId = eventId,
                    name = user.name,
                    email = user.email
                )
            )

        emit(Unit)
    }.flowOn(appDispatchers.io)

    private fun assertValidUser(user: User) {
        if (user == User.Empty) {
            throw AppNoLoggedUserException()
        }
    }
}