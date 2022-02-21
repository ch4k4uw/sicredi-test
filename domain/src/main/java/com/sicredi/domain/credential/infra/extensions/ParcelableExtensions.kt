package com.sicredi.domain.credential.infra.extensions

import android.os.Parcel
import android.os.Parcelable
import android.util.Base64
import timber.log.Timber

internal fun Parcelable.marshall(): String {
    val parcel = Parcel.obtain()
    writeToParcel(parcel, 0)
    val result = parcel.marshall().let { Base64.encodeToString(it, Base64.NO_WRAP) }
    parcel.recycle()
    return result
}

internal inline fun <reified T> Parcelable.Creator<*>.unmarshall(
    data: ByteArray,
    defaultValue: T
): T = if (data.isNotEmpty()) {
    val parcel = Parcel.obtain()
    parcel.unmarshall(data, 0, data.size)
    parcel.setDataPosition(0)
    val result = createFromParcel(parcel).let { it as T }
    parcel.recycle()
    Timber.d(result.toString())
    result
} else {
    defaultValue
}