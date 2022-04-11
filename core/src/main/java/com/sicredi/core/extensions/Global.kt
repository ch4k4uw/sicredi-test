package com.sicredi.core.extensions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sicredi.core.ui.compose.AppTheme

@Composable
fun rememberBitmapResult(
    uri: Uri
): State<Result<Bitmap>?> {
    val result = remember { mutableStateOf(null as Result<Bitmap>?) }

    val context = LocalContext.current
    LaunchedEffect(uri, context) {
        if (uri.toString().isNotBlank()) {
            Glide
                .with(context)
                .asBitmap()
                .load(uri)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        result.value = Result.success(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        result.value = Result.failure(Exception())
                    }
                })
        }
    }
    return result
}