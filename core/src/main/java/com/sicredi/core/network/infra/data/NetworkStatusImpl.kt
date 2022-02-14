package com.sicredi.core.network.infra.data

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.sicredi.core.network.domain.data.NetworkStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber

internal class NetworkStatusImpl(
    @ApplicationContext private val context: Context
) : NetworkStatus {
    override val hasInternetConnection: Boolean
        get() = hasInternetConnection()

    @SuppressLint("MissingPermission")
    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            Timber.e("Cannot use this method for the current API Level")
            true
        }
    }
}