package com.sicredi.presenter.common.extensions

import android.os.Parcelable
import kotlin.reflect.KClass

val <T : Parcelable> KClass<out T>.creator: Parcelable.Creator<*>
    get() = java
        .fields
        .find { it.name == "CREATOR" }
        ?.get(null)
        ?.let { it as? Parcelable.Creator<*> }
        ?: throw RuntimeException("${java.name}::CREATOR not found")