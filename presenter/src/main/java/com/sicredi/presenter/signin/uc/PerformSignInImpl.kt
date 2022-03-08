package com.sicredi.presenter.signin.uc

import com.sicredi.domain.credential.domain.entity.User
import com.sicredi.domain.credential.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PerformSignInImpl @Inject constructor(
    private val userRepository: UserRepository
) : PerformSignIn {
    override suspend fun invoke(email: String, password: String): Flow<User> =
        userRepository.signIn(email = email, password = password)
}