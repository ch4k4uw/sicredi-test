package com.sicredi.presenter.common.uc

import kotlinx.coroutines.flow.Flow

interface PerformLogout {
    suspend operator fun invoke(): Flow<Unit>
}