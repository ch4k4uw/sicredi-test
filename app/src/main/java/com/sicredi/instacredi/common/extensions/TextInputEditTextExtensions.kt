package com.sicredi.instacredi.common.extensions

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import com.google.android.material.textfield.TextInputEditText

var TextInputEditText.sText: String?
    get() = text?.toString()
    set(value) {
        setText(value)
    }

fun TextInputEditText.showKeyboard() {
    post {
        val imm: InputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun TextInputEditText.requestSelection(showSoftInput: Boolean = true) {
    requestFocus()
    setSelection(0, sText?.length ?: 0)
    if (showSoftInput) {
        showKeyboard()
    }
}