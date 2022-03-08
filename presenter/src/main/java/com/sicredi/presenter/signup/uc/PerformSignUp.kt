package com.sicredi.presenter.signup.uc

import com.sicredi.domain.credential.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface PerformSignUp {
    suspend operator fun invoke(name: String, email: String, password: String): Flow<User>
}