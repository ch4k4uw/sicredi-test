package com.sicredi.core.extensions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

@Composable
fun rememberBitmapResult(
    uri: Uri
): State<Result<Bitmap>?> {
    val result = remember { mutableStateOf(null as Result<Bitmap>?) }
    if (uri.toString().isNotBlank()) {
        Glide
            .with(LocalContext.current)
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
    return result
}