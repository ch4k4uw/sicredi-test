package com.sicredi.core.network.domain.data.factory

import okhttp3.OkHttpClient

interface HttpClientFactory {
    fun create(): OkHttpClient
}