package com.sicredi.instacredi.common.extensions

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.sicredi.core.extensions.showAppWarningFragment
import com.sicredi.core.ui.component.AppWarningFragment
import com.sicredi.instacredi.R
import com.sicredi.instacredi.common.showProfileBottomSheetFragment

fun Fragment.navHostFragment(navHostId: Int): Lazy<NavHostFragment> = lazy {
    return@lazy childFragmentManager.findFragmentById(navHostId) as NavHostFragment
}

val Fragment.dataStore: DataStore<Preferences>
    get() = requireActivity().dataStore

fun Fragment.hideKeyboard() {
    val imm: InputMethodManager =
        context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.rootView?.windowToken, 0)
}

fun Fragment.showProfileBottomSheetFragment(name: String, email: String) {
    childFragmentManager.showProfileBottomSheetFragment(name = name, email = email)
}

fun Fragment.showError(isGenericError: Boolean, requestKey: String, optionParams: Bundle? = null) {
    val title = if (isGenericError) {
        R.string.app_generic_error_title
    } else {
        R.string.app_no_connectivity_generic_error_title
    }
    val description = if (isGenericError) {
        R.string.app_generic_error_description
    } else {
        R.string.app_no_connectivity_generic_error_description
    }
    val primaryLabel = if (isGenericError) {
        R.string.app_generic_error_positive_action
    } else {
        R.string.app_no_connectivity_generic_error_positive_action
    }
    val secondaryLabel = if (isGenericError) {
        R.string.app_generic_error_negative_action
    } else {
        R.string.app_no_connectivity_generic_error_negative_action
    }
    val icon = if (isGenericError) {
        android.R.drawable.ic_menu_close_clear_cancel
    } else {
        android.R.drawable.stat_sys_warning
    }
    val color = if (isGenericError) {
        AppWarningFragment.BarColor.RED
    } else {
        AppWarningFragment.BarColor.YELLOW
    }
    showAppWarningFragment(requestKey = requestKey) {
        if (optionParams != null) {
            optionParams(optionParams = optionParams)
        }
        title(getString(title))
        barColor(color)
        icon(icon)
        description(getString(description))
        primaryButtonText(getString(primaryLabel))
        secondaryButtonText(getString(secondaryLabel))
    }
}