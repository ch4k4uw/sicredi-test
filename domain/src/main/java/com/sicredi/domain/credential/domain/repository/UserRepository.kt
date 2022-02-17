package com.sicredi.domain.credential.domain.repository

import com.sicredi.domain.credential.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun findLogged(): Flow<User>
    suspend fun signIn(email: String, password: String): Flow<User>
}