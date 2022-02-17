package com.sicredi.instacredi.signin.uc

import com.sicredi.domain.credential.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface PerformSignIn {
    suspend operator fun invoke(email: String, password: String): Flow<User>
}