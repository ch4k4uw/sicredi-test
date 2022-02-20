package com.sicredi.instacredi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sicredi.core.ui.component.AppWarningFragment
import com.sicredi.core.ui.component.dismissAppWarningFragment
import com.sicredi.core.ui.component.showAppWarningFragment
import com.sicredi.instacredi.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        supportFragmentManager.setFragmentResultListener(
            "warning-dialog-test-1",
            this
        ) { _, result ->
            val isPrimary = result[AppWarningFragment.Companion.Key.ActionId] ==
                    AppWarningFragment.ACTION_PRIMARY
            val buttonId = if (isPrimary) "primary" else "secondary"

            Timber.i("Ok, I received the signal of \"$buttonId\" button")
            supportFragmentManager.dismissAppWarningFragment()
            lifecycleScope.launch {
                delay(2000)
                supportFragmentManager
                    .showAppWarningFragment(requestKey = "warning-dialog-test-2") {
                        title("Test title 2")
                        barColor(AppWarningFragment.BarColor.RED)
                        description("This is the other test.\nSo now, is it ok?")
                        primaryButtonText("Yes")
                        secondaryButtonText("No")
                    }
            }
        }

        supportFragmentManager.setFragmentResultListener(
            "warning-dialog-test-2",
            this
        ) { _, result ->
            Timber.i("Result 2: $result")
            supportFragmentManager.dismissAppWarningFragment()
        }

        lifecycleScope.launchWhenResumed {
            supportFragmentManager
                .showAppWarningFragment(requestKey = "warning-dialog-test-1") {
                    title("Test title")
                    barColor(AppWarningFragment.BarColor.YELLOW)
                    description("This is some random description put to test this dialog")
                    primaryButtonText("Ok")
                    secondaryButtonText("Cancel")
                }
        }
    }
}