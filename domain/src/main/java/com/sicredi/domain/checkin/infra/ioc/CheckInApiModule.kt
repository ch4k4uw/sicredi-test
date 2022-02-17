package com.sicredi.domain.checkin.infra.ioc

import com.sicredi.core.network.infra.service.HttpServiceFactory
import com.sicredi.domain.BuildConfig
import com.sicredi.domain.checkin.infra.remote.CheckInApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CheckInApiModule {
    @Provides
    fun provideCheckInApi(serviceFactory: HttpServiceFactory): CheckInApi =
        serviceFactory.createService(
            serviceClass = CheckInApi::class,
            baseUrl = BuildConfig.INSTACREDI_API_URL
        )
}