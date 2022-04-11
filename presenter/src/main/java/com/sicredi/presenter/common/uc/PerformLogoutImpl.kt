package com.sicredi.presenter.common.uc

import com.sicredi.domain.credential.domain.repository.UserCmdRepository
import com.sicredi.presenter.common.uc.PerformLogout
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PerformLogoutImpl @Inject constructor(
    private val userRepository: UserCmdRepository
) : PerformLogout {
    override suspend fun invoke(): Flow<Unit> =
        userRepository.logout()
}