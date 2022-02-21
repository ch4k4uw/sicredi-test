package com.sicredi.instacredi.common.extensions

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

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