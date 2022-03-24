package com.sicredi.instacredi.common.extensions

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import com.sicredi.presenter.common.extensions.creator
import com.sicredi.presenter.common.extensions.unmarshall
import kotlin.reflect.KClass

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.viewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    vararg marshalledArgs: Pair<String, KClass<out Parcelable>>
): T {
    marshalledArgs.forEach { unmarshallArg(it.first, it.second) }
    val factory = HiltViewModelFactory(LocalContext.current, this)
    return androidx.lifecycle.viewmodel.compose.viewModel(
        viewModelStoreOwner = viewModelStoreOwner, key = T::class.java.name, factory = factory
    )
}

fun <T: Parcelable> NavBackStackEntry.unmarshallArg(
    key: String,
    clazz: KClass<T>,
): Bundle? {
    return arguments
        ?.also { args ->
            val rawValue = args.get(key) as? String
            if (rawValue != null) {
                val value: T = clazz.creator.unmarshall(
                    source = rawValue
                )
                args.remove(key)
                args.putParcelable(key, value)
            }
        }
}