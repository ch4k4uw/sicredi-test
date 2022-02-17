package com.sicredi.core.ui.component

import android.animation.LayoutTransition
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import com.sicredi.core.R

class AppContentLoadingProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    var progressReferences: Int = 0
        set(value) {
            val normalizedValue = if (value < 0) {
                0
            } else {
                value
            }
            field = normalizedValue
            if (normalizedValue == 1) {
                post {
                    super.setVisibility(View.VISIBLE)
                }
            } else if (normalizedValue == 0) {
                post {
                    super.setVisibility(View.GONE)
                }
            }
        }

    override fun setVisibility(visibility: Int) {
        progressReferences += if (visibility == View.GONE || visibility == View.INVISIBLE)
            -1
        else
            1
    }

    init {
        layoutTransition = LayoutTransition()
        background = ColorDrawable((0x66000000).toInt())
        isClickable = true
        isFocusable = true

        addView(createProgressBar())
    }

    private fun createProgressBar(): ProgressBar =
        ContentLoadingProgressBar(
            ContextThemeWrapper(
                context,
                resolveProgressStyle()
            )
        ).also { progress ->
            progress.layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).also { params ->
                params.gravity = Gravity.CENTER
            }
            progress.alpha = 1F
            progress.isIndeterminate = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                progress.indeterminateTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.secondary))
            }
        }

    private fun resolveProgressStyle(): Int {
        return TypedValue().let {
            if (context.theme.resolveAttribute(android.R.attr.progressBarStyle, it, true)) {
                it.resourceId
            } else {
                android.R.style.Widget_ProgressBar
            }
        }
    }
}