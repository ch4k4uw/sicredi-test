package com.sicredi.core.ui.component

fun interface AppAsyncImageLoadingListener {
    fun onLoadingStatusChanged(
        status: AppAsyncImageLoadingStatus, view: AppAsyncImageView
    )
}