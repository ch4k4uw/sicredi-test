package com.sicredi.core.network.infra.injection

import android.content.Context
import com.sicredi.core.network.domain.data.NetworkStatus
import com.sicredi.core.network.domain.data.factory.HttpClientFactory
import com.sicredi.core.network.infra.data.NetworkStatusImpl
import com.sicredi.core.network.infra.data.factory.HttpClientFactoryImpl
import com.sicredi.core.network.infra.service.HttpLoggerFactory
import com.sicredi.core.network.infra.service.HttpServiceFactory
import com.sicredi.core.network.infra.service.RequestHandlerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideHttpLoggerFactory(): HttpLoggerFactory =
        HttpLoggerFactory()

    @Provides
    fun provideNetworkStatus(@ApplicationContext context: Context): NetworkStatus =
        NetworkStatusImpl(context = context)

    @Provides
    @Singleton
    fun provideHttpClientFactory(
        networkStatus: NetworkStatus,
        httpLoggerFactory: HttpLoggerFactory,
        requestInterceptorProvider: Provider<RequestHandlerInterceptor>
    ): HttpClientFactory = HttpClientFactoryImpl(
        networkStatus = networkStatus,
        loggerFactory = httpLoggerFactory,
        requestInterceptorProvider = requestInterceptorProvider
    )

    @Provides
    @Singleton
    fun provideHttpServiceFactory(
        httpClientFactory: HttpClientFactory
    ) = HttpServiceFactory(httpClientFactory = httpClientFactory)

}