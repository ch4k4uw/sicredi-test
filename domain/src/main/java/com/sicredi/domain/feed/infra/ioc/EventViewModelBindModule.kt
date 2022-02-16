package com.sicredi.domain.feed.infra.ioc

import com.sicredi.domain.feed.domain.repository.EventRepository
import com.sicredi.domain.feed.infra.repository.EventRepositoryImpl
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
    abstract fun bindEventRepository(impl: EventRepositoryImpl): EventRepository
}