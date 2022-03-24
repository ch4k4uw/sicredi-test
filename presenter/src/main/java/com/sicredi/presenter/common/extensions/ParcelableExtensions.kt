package com.sicredi.presenter.common.extensions

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.util.Base64

fun Parcelable.marshall(): String {
    val parcel = Parcel.obtain()
    writeToParcel(parcel, 0)
    val result = parcel.marshall().let { Base64.encodeToString(it, Base64.NO_WRAP) }
    parcel.recycle()
    return Uri.encode(result)
}

@Suppress("UNCHECKED_CAST")
fun <T : Parcelable> Parcelable.Creator<*>.unmarshall(
    source: String
): T = if (source.isNotEmpty()) {
    val data = Base64.decode(Uri.decode(source), Base64.NO_WRAP)
    val parcel = Parcel.obtain()
    parcel.unmarshall(data, 0, data.size)
    parcel.setDataPosition(0)
    val result = createFromParcel(parcel).let { it as T }
    parcel.recycle()
    result
} else {
    throw RuntimeException("Unable to unmarshall empty value")
}