package com.sicredi.instacredi

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sicredi.core.ui.component.AppWarningFragment
import com.sicredi.core.ui.component.AppWarningFragmentListener
import com.sicredi.instacredi.databinding.ActivityMainBinding
import com.sicredi.instacredi.feed.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AppWarningFragmentListener {
    private lateinit var viewBinding: ActivityMainBinding
    private val feedViewModel: FeedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        lifecycleScope.launchWhenResumed {
            AppWarningFragment.Builder()
                .title("Test title")
                .barColor(AppWarningFragment.BarColor.YELLOW)
                .description("This is some random description put to test this dialog")
                .primaryButtonText("Ok")
                .secondaryButtonText("Cancel")
                .build()
                .show(supportFragmentManager, "confirmationTest")
        }
    }

    override fun onWarningFragmentButtonClick(
        isPrimary: Boolean,
        dialog: BottomSheetDialogFragment
    ) {
        if (dialog.tag == "confirmationTest") {
            val buttonId = if (isPrimary) "primary" else "secondary"
            Timber.i("Ok, I received the signal of \"$buttonId\" button")
            lifecycleScope.launch {
                delay(2000)
                AppWarningFragment.Builder()
                    .title("Test title 2")
                    .barColor(AppWarningFragment.BarColor.YELLOW)
                    .description("This is the other test.\nSo now, is it ok?")
                    .primaryButtonText("Yes")
                    .secondaryButtonText("No")
                    .build()
                    .show(supportFragmentManager, "confirmationTest_2")
            }
        } else {
            val source = dialog.tag ?: "unknown"
            Timber.i("Unexpected source: $source")
        }
        dialog.dismiss()
    }
}