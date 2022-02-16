package com.sicredi.domain.feed.infra.ioc

import com.sicredi.core.network.infra.service.HttpServiceFactory
import com.sicredi.domain.BuildConfig
import com.sicredi.domain.feed.infra.remote.EventApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class EventApiModule {
    @Provides
    fun provideEventApi(serviceFactory: HttpServiceFactory): EventApi =
        serviceFactory.createService(
            serviceClass = EventApi::class,
            baseUrl = BuildConfig.INSTACREDI_API_URL
        )
}