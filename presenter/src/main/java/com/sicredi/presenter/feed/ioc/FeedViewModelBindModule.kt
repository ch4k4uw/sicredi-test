package com.sicredi.presenter.feed.ioc

import com.sicredi.presenter.feed.uc.FindAllEvents
import com.sicredi.presenter.feed.uc.FindAllEventsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class FeedViewModelBindModule {
    @Binds
    @ViewModelScoped
    abstract fun bindFindAllEvents(impl: FindAllEventsImpl): FindAllEvents
}