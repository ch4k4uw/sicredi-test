package com.sicredi.core.ui.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Parcelable
import android.util.AttributeSet
import androidx.annotation.MainThread
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AppAsyncImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    private var coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var loadingJob: Job? = null
    private var loadingState: ThumbnailLoadingState? = null

    var imageLoadingListener: AppAsyncImageLoadingListener? = null

    @MainThread
    fun loadImage(
        uri: Uri,
        size: Int = AppAsyncImageViewDefaults.DefaultImageSize
    ) {
        loadingState = ThumbnailLoadingState(uri = uri, size = size)
        val currJob = loadingJob
        if (currJob != null) {
            currJob.cancel()
            loadingJob = null
        }

        notifyLoadingStarting()

        setImageDrawable(
            AppCompatResources.getDrawable(context, AppAsyncImageViewDefaults.LoadingPlaceholder)
        )

        loadingJob = coroutineScope.launch {
            withContext(Dispatchers.Main) {
                notifyLoadingStarted()
            }
            val bitmap = try {
                thirdPartyLoadImage(uri = uri, size = size)
            } catch (cause: Throwable) {
                Timber.e(cause)
                null
            }
            withContext(Dispatchers.Main) {
                if (loadingJob?.isActive == true) {
                    if (bitmap == null) {
                        setImageDrawable(
                            AppCompatResources
                                .getDrawable(context, AppAsyncImageViewDefaults.ErrorPlaceholder)
                        )
                    } else {
                        setImageBitmap(bitmap)
                    }
                    loadingJob = null
                    if (bitmap == null) notifyLoadingError() else notifyLoadingSuccess()
                }
            }
        }
    }

    private fun notifyLoadingStarting() {
        notifyLoadingStatus(status = AppAsyncImageLoadingStatus.Starting)
    }

    private fun notifyLoadingStatus(status: AppAsyncImageLoadingStatus) {
        imageLoadingListener?.onLoadingStatusChanged(status = status, view = this)
    }

    private fun notifyLoadingStarted() {
        notifyLoadingStatus(status = AppAsyncImageLoadingStatus.Loading)
    }

    private suspend fun thirdPartyLoadImage(
        uri: Uri,
        size: Int,
    ): Bitmap? {
        val isResize = size != AppAsyncImageViewDefaults.DefaultImageSize
        val normalizedSize = if(isResize) size else Target.SIZE_ORIGINAL
        val requestOptions = if (isResize) {
            RequestOptions()
                .override(normalizedSize)
                .downsample(DownsampleStrategy.CENTER_INSIDE)
            //  .skipMemoryCache(true)
            //  .diskCacheStrategy(DiskCacheStrategy.NONE)
        } else {
            RequestOptions()
        }

        return suspendCancellableCoroutine { continuation ->
            Glide
                .with(context)
                .asBitmap()
                .load(uri)
                .run {
                    if (isResize) apply(requestOptions) else this
                }
                .into(createCustomTarget(size = normalizedSize, continuation = continuation))
        }
    }

    private fun createCustomTarget(size: Int, continuation: Continuation<Bitmap?>) =
        object : CustomTarget<Bitmap>(size, size) {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                try {
                    continuation.resume(value = resource)
                } catch (cause: Throwable) {
                    continuation.resumeWithException(exception = cause)
                }
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                continuation.resumeWithException(Exception())
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                continuation.resume(value = null)
            }
        }

    private fun notifyLoadingError() {
        notifyLoadingStatus(status = AppAsyncImageLoadingStatus.Error)
    }

    private fun notifyLoadingSuccess() {
        notifyLoadingStatus(status = AppAsyncImageLoadingStatus.Success)
    }

    override fun onSaveInstanceState(): Parcelable {
        coroutineScope.cancel(message = "Saving state")
        return AppAsyncImageState(
            source = super.onSaveInstanceState(),
            thumbnailLoading = loadingState
        )
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state == null || state !is AppAsyncImageState) {
            super.onRestoreInstanceState(state)
        } else {
            val source = state.source
            loadingState = state.thumbnailLoading

            if (source != null) {
                super.onRestoreInstanceState(state)
            }
            val thumbnailLoadingState = loadingState
            if (thumbnailLoadingState != null) {
                loadImage(uri = thumbnailLoadingState.uri, size = thumbnailLoadingState.size)
            }
        }
    }

    override fun setImageDrawable(drawable: Drawable?) {
        loadingState = null
        super.setImageDrawable(drawable)
    }

    override fun setImageResource(resId: Int) {
        loadingState = null
        super.setImageResource(resId)
    }

    @Parcelize
    private data class AppAsyncImageState(
        val source: Parcelable?,
        val thumbnailLoading: ThumbnailLoadingState?
    ) : Parcelable

    @Parcelize
    private data class ThumbnailLoadingState(
        val uri: Uri,
        val size: Int
    ) : Parcelable
}