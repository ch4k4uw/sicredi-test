package com.sicredi.core.network.infra.data.factory

import com.sicredi.core.network.domain.data.NetworkStatus
import com.sicredi.core.network.domain.data.NoConnectivityException
import com.sicredi.core.network.domain.data.factory.HttpClientFactory
import com.sicredi.core.network.infra.service.HttpLoggerFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

internal class HttpClientFactoryImpl constructor(
    private val networkStatus: NetworkStatus,
    private val loggerFactory: HttpLoggerFactory
) : HttpClientFactory {
    override fun create(): OkHttpClient = OkHttpClient.Builder().let { builder ->
        builder.addNetworkInterceptor(loggerFactory.create())
        builder.addNetworkInterceptor {
            if (!networkStatus.hasInternetConnection) throw NoConnectivityException()
            it.proceed(it.request())
        }
        builder
            .readTimeout(180L, TimeUnit.SECONDS)
            .writeTimeout(180L, TimeUnit.SECONDS)
            .connectTimeout(180L, TimeUnit.SECONDS)
        builder.build()
    }

}