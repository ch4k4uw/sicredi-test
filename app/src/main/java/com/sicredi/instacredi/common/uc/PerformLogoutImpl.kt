package com.sicredi.instacredi.common.uc

import com.sicredi.domain.credential.domain.repository.UserCmdRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PerformLogoutImpl @Inject constructor(
    private val userRepository: UserCmdRepository
) : PerformLogout {
    override suspend fun invoke(): Flow<Unit> =
        userRepository.logout()
}