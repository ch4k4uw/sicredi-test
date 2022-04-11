package com.sicredi.presenter.common.uc

import com.sicredi.domain.credential.domain.entity.User
import com.sicredi.domain.credential.domain.repository.UserRepository
import com.sicredi.presenter.common.uc.FindLoggedUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindLoggedUserImpl @Inject constructor(
    private val userRepository: UserRepository
) : FindLoggedUser {
    override suspend fun invoke(): Flow<User> =
        userRepository.findLogged()
}