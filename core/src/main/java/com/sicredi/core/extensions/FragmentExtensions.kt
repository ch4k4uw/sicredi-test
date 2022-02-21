package com.sicredi.core.extensions

import androidx.fragment.app.Fragment
import com.sicredi.core.ui.component.AppWarningFragment
import com.sicredi.core.ui.component.dismissAppWarningFragment
import com.sicredi.core.ui.component.showAppWarningFragment

fun Fragment.showAppWarningFragment(
    requestKey: String,
    tag: String = AppWarningFragment::class.simpleName!!,
    builder: AppWarningFragment.Builder.() -> AppWarningFragment.Builder
) {
    childFragmentManager.showAppWarningFragment(
        requestKey = requestKey, tag = tag, builder = builder
    )
}

fun Fragment.dismissAppWarningFragment(
    tag: String = AppWarningFragment::class.simpleName!!
) {
    childFragmentManager.dismissAppWarningFragment(tag = tag)
}