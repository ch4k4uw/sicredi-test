package com.sicredi.domain.checkin.infra.ioc

import com.sicredi.domain.checkin.domain.repository.CheckInCmdRepository
import com.sicredi.domain.checkin.infra.repository.CheckInCmdRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class CheckInViewModelBindModule {
    @Binds
    @ViewModelScoped
    abstract fun bindCheckInRepository(impl: CheckInCmdRepositoryImpl): CheckInCmdRepository
}