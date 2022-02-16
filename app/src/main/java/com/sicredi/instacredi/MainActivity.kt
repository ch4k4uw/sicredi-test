package com.sicredi.instacredi

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.sicredi.instacredi.databinding.ActivityMainBinding
import com.sicredi.instacredi.feed.FeedViewModel
import com.sicredi.instacredi.feed.interaction.FeedState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private val feedViewModel: FeedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        lifecycleScope.launchWhenResumed {
            feedViewModel.state.observe(this@MainActivity) { state ->
                when (state) {
                    FeedState.Loading -> {
                        viewBinding.appImageDownload.setImageDrawable(
                            AppCompatResources.getDrawable(
                                this@MainActivity,
                                com.sicredi.core.R.drawable.ic_downloading_gray900_24,
                            )
                        )
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            viewBinding.appImageDownload.imageTintList = ColorStateList.valueOf(
                                Color.BLUE
                            )
                        }
                    }
                    is FeedState.FeedNotLoaded -> {
                        viewBinding.appImageDownload.setImageDrawable(
                            AppCompatResources.getDrawable(
                                this@MainActivity,
                                com.sicredi.core.R.drawable.ic_broken_image_gray900_24,
                            )
                        )
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            viewBinding.appImageDownload.imageTintList = ColorStateList.valueOf(
                                Color.RED
                            )
                        }
                    }
                    is FeedState.FeedSuccessfulLoaded -> {
                        viewBinding.appImageDownload.setImageDrawable(null)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            viewBinding.appImageDownload.imageTintList = null
                        }
                        Timber.i(state.eventHeads.toString())
                        lifecycleScope.launch {
                            delay(1000)
                            feedViewModel.findDetail(id = state.eventHeads[0].id)
                        }
                    }
                    is FeedState.EventDetailsNotLoaded -> {
                        viewBinding.appImageDownload.setImageDrawable(
                            AppCompatResources.getDrawable(
                                this@MainActivity,
                                com.sicredi.core.R.drawable.ic_broken_image_gray900_24,
                            )
                        )
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            viewBinding.appImageDownload.imageTintList = ColorStateList.valueOf(
                                Color.RED
                            )
                        }
                    }
                    is FeedState.EventDetailsSuccessfulLoaded -> {
                        viewBinding.appImageDownload.setImageDrawable(null)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            viewBinding.appImageDownload.imageTintList = null
                        }
                        Timber.i(state.details.toString())
                    }
                }
            }
            feedViewModel.loadFeed()
        }
    }
}