package com.sicredi.instacredi.signup.uc

import com.sicredi.domain.credential.domain.entity.User
import com.sicredi.domain.credential.domain.repository.UserCmdRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PerformSignUpImpl @Inject constructor(
    private val userRepository: UserCmdRepository
) : PerformSignUp {
    override suspend fun invoke(name: String, email: String, password: String): Flow<User> =
        userRepository.signUp(User(id = "", name = name, email = email), password = password)
}