package com.sicredi.domain.credential.domain.repository

import com.sicredi.domain.credential.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface UserCmdRepository : UserRepository {
    suspend fun signUp(user: User, password: String): Flow<User>
    suspend fun logout(): Flow<Unit>
}