package com.sicredi.core.ui.component

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

fun interface AppWarningFragmentListener {
    fun onWarningFragmentButtonClick(isPrimary: Boolean, dialog: BottomSheetDialogFragment)
}