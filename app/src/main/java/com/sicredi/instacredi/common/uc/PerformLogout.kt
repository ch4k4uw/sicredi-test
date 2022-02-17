package com.sicredi.instacredi.common.uc

import kotlinx.coroutines.flow.Flow

interface PerformLogout {
    suspend operator fun invoke(): Flow<Unit>
}