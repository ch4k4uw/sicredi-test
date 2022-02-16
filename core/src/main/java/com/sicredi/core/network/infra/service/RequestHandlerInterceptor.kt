package com.sicredi.core.network.infra.service

import com.sicredi.core.network.domain.data.AppHttpBadRequestException
import com.sicredi.core.network.domain.data.AppHttpException
import com.sicredi.core.network.domain.data.AppHttpGenericException
import com.sicredi.core.network.domain.data.AppHttpInternalServerException
import com.sicredi.core.network.domain.data.AppHttpNotFountException
import com.sicredi.core.network.domain.data.AppHttpUnknownException
import com.sicredi.core.network.domain.data.HttpConstants
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class RequestHandlerInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        try {
            val response = chain.proceed(requestBuilder.build())
            if (response.isSuccessful || response.code in 300..399) {
                return response
            } else {
                Timber.w(response.toString())
                throw when (response.code) {
                    HttpConstants.NOT_FOUND_ERROR -> AppHttpNotFountException
                    HttpConstants.BAD_REQUEST -> AppHttpBadRequestException
                    HttpConstants.INTERNAL_ERROR -> AppHttpInternalServerException
                    else -> AppHttpGenericException(response.code)
                }
            }
        } catch (cause: Throwable) {
            if (cause is AppHttpException) throw cause
            else throw AppHttpUnknownException
        }
    }
}