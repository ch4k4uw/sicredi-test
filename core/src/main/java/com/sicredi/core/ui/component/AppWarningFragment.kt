package com.sicredi.core.ui.component

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sicredi.core.R
import com.sicredi.core.databinding.FragmentWarningBinding

class AppWarningFragment : BottomSheetDialogFragment() {
    private lateinit var viewBinding: FragmentWarningBinding

    companion object {
        object Key {
            internal const val RequestKey = "request.key"
            internal const val Title = "title"
            internal const val Description = "description"
            internal const val PrimaryButtonText = "button.primary.text"
            internal const val SecondaryButtonText = "button.secondary.text"
            internal const val Icon = "icon"
            internal const val BarColor = "bar.color"
            const val ActionId = "result.action"
        }
        const val ACTION_PRIMARY = "primary"
        const val ACTION_SECONDARY = "secondary"
    }

    private val requestKey: String
        get() = arguments?.getString(Key.RequestKey) ?: ""

    private val title: String
        get() = arguments?.getString(Key.Title) ?: ""

    private val description: String
        get() = arguments?.getString(Key.Description) ?: ""

    private val primaryButtonText: String?
        get() = arguments?.getString(Key.PrimaryButtonText)

    private val secondaryButtonText: String?
        get() = arguments?.getString(Key.SecondaryButtonText)

    private val icon: Int
        get() = arguments?.getInt(Key.Icon) ?: 0

    private val barColor: BarColor
        get() = arguments?.getInt(Key.BarColor)?.let { BarColor.values()[it] } ?: BarColor.GREEN

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialogWithBehavior = LockedTopRoundedBottomSheetDialog(requireContext())
        //isCancelable = primaryButtonText == null && secondaryButtonText == null
        return bottomSheetDialogWithBehavior.setBehavior(super.onCreateDialog(savedInstanceState))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentWarningBinding.inflate(inflater, container, false).run {
        viewBinding = this
        root
    }

    override fun getTheme(): Int =
        R.style.Theme_Core_WarningBottomSheetDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBottomSheetConfigs()
    }

    private fun notifyButtonClick(isPrimary: Boolean) {
        parentFragmentManager.setFragmentResult(
            requestKey, bundleOf(
                Key.ActionId to if (isPrimary) ACTION_PRIMARY else ACTION_SECONDARY
            )
        )
    }

    private fun handleBottomSheetConfigs() {
        with(viewBinding) {
            titleTextView.text = title
            primaryButtonText?.let { buttonText ->
                primaryButton.apply {
                    text = buttonText
                    visibility = View.VISIBLE
                    setOnClickListener {
                        notifyButtonClick(isPrimary = true)
                    }
                }
            }
            secondaryButtonText?.let { buttonText ->
                secondaryButton.apply {
                    text = buttonText
                    visibility = View.VISIBLE
                    setOnClickListener {
                        notifyButtonClick(isPrimary = false)
                    }
                }
            }

            descriptionTextView.text = description

            iconImageView.setImageResource(icon)

            when (barColor) {
                BarColor.GREEN -> divisor.setBackgroundResource(R.drawable.rounded_line_green_background)
                BarColor.YELLOW -> divisor.setBackgroundResource(R.drawable.rounded_line_yellow_background)
                BarColor.RED -> divisor.setBackgroundResource(R.drawable.rounded_line_red_background)
            }
        }
    }

    enum class BarColor { RED, YELLOW, GREEN }

    class Builder(private val requestKey: String) {
        private var title: String = ""
        private var description: String = ""
        private var icon: Int = android.R.drawable.ic_menu_close_clear_cancel
        private var barColor: BarColor = BarColor.GREEN
        private var primaryButtonText: String? = null
        private var secondaryButtonText: String? = null

        fun title(newTitle: String) = apply { this.title = newTitle }

        fun description(newDescription: String) = apply { this.description = newDescription }

        fun primaryButtonText(newValue: String) = apply { this.primaryButtonText = newValue }

        fun secondaryButtonText(newValue: String) = apply { this.secondaryButtonText = newValue }

        fun icon(newIcon: Int) = apply { this.icon = newIcon }

        fun barColor(newBarColor: BarColor) = apply { this.barColor = newBarColor }

        fun build() = AppWarningFragment().also { dialog ->
            dialog.arguments = bundleOf(
                Companion.Key.RequestKey to requestKey,
                Companion.Key.Title to requestKey,
                Companion.Key.Description to description,
                Companion.Key.Icon to icon,
                Companion.Key.BarColor to barColor.ordinal,
                Companion.Key.PrimaryButtonText to primaryButtonText,
                Companion.Key.SecondaryButtonText to secondaryButtonText,
            )
        }

    }
}

fun FragmentManager.showAppWarningFragment(
    requestKey: String,
    tag: String = AppWarningFragment::class.simpleName!!,
    builder: AppWarningFragment.Builder.() -> AppWarningFragment.Builder
) {
    AppWarningFragment.Builder(requestKey = requestKey).builder().build().show(this, tag)
}

fun FragmentManager.dismissAppWarningFragment(
    tag: String = AppWarningFragment::class.simpleName!!
) {
    (findFragmentByTag(tag) as? AppWarningFragment)
        ?.dismiss()
}