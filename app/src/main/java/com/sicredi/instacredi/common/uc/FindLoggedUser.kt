package com.sicredi.instacredi.common.uc

import com.sicredi.domain.credential.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface FindLoggedUser {
    suspend operator fun invoke(): Flow<User>
}