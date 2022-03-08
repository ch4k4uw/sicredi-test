package com.sicredi.presenter.event.ioc

import com.sicredi.presenter.event.uc.PerformCheckIn
import com.sicredi.presenter.event.uc.PerformCheckInImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class EventViewModelBindModule {
    @Binds
    @ViewModelScoped
    abstract fun bindPerformCheckIn(impl: PerformCheckInImpl): PerformCheckIn
}