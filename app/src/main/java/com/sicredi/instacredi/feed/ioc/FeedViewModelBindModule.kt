package com.sicredi.instacredi.feed.ioc

import com.sicredi.instacredi.feed.uc.FindAllEvents
import com.sicredi.instacredi.feed.uc.FindAllEventsImpl
import com.sicredi.instacredi.feed.uc.FindEventDetails
import com.sicredi.instacredi.feed.uc.FindEventDetailsImpl
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

    @Binds
    @ViewModelScoped
    abstract fun bindFindEventDetails(impl: FindEventDetailsImpl): FindEventDetails
}