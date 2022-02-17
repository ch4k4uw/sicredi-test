package com.sicredi.core.ui.component

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.sicredi.core.R

class LockedTopRoundedBottomSheetDialog(private val context: Context) {

    fun setBehavior(bottomSheetDialog: Dialog): Dialog {
        val dialog = bottomSheetDialog as BottomSheetDialog

        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.setOnShowListener {
            val bottomSheet =
                (it as BottomSheetDialog)
                    .findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)

            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    val newMaterialShapeDrawable =
                        createMaterialShapeDrawable(bottomSheet)
                    ViewCompat.setBackground(bottomSheet, newMaterialShapeDrawable)
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    /** Function not needed **/
                }
            })
        }

        return dialog
    }

    private fun createMaterialShapeDrawable(bottomSheet: View): MaterialShapeDrawable? {
        val shapeAppearanceModel =
            ShapeAppearanceModel.builder(
                context,
                0,
                R.style.ShapeAppearanceBottomSheetDialog_Rounded
            )
                .build()

        val currentMaterialShapeDrawable =
            bottomSheet.background as MaterialShapeDrawable
        val newMaterialShapeDrawable =
            MaterialShapeDrawable(shapeAppearanceModel)

        newMaterialShapeDrawable.initializeElevationOverlay(context)
        newMaterialShapeDrawable.fillColor = currentMaterialShapeDrawable.fillColor
        newMaterialShapeDrawable.tintList = currentMaterialShapeDrawable.tintList
        newMaterialShapeDrawable.elevation = currentMaterialShapeDrawable.elevation
        newMaterialShapeDrawable.strokeWidth = currentMaterialShapeDrawable.strokeWidth
        newMaterialShapeDrawable.strokeColor = currentMaterialShapeDrawable.strokeColor
        return newMaterialShapeDrawable
    }

}