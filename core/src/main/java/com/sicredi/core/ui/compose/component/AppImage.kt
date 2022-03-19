package com.sicredi.core.ui.compose.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.sicredi.core.extensions.rememberBitmapResult

@Composable
fun AppImage(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    uri: Uri
) {
    val rawImageResult = rememberBitmapResult(uri = uri)
    val imageResultValue = rawImageResult.value
    if (imageResultValue != null) {
        val image = imageResultValue.getOrNull()
        if(image != null) {
            Image(
                modifier = modifier,
                bitmap = image.asImageBitmap(),
                contentScale = contentScale,
                contentDescription = null
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
            ) {
                Icon(imageVector = Icons.Default.BrokenImage, contentDescription = null)
            }
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
        ) {
            Icon(imageVector = Icons.Default.Image, contentDescription = null)
        }
    }
}