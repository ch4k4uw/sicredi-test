package com.sicredi.instacredi

import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.sicredi.core.ui.component.AppAsyncImageLoadingListener
import com.sicredi.core.ui.component.AppAsyncImageLoadingStatus
import com.sicredi.core.ui.component.AppAsyncImageView
import com.sicredi.core.ui.component.AppAsyncImageViewDefaults
import com.sicredi.instacredi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        viewBinding.appImageDownload.imageLoadingListener =
            AppAsyncImageLoadingListener { status, view ->
                when(status) {
                    AppAsyncImageLoadingStatus.Starting -> ColorStateList.valueOf(Color.GREEN)
                    AppAsyncImageLoadingStatus.Loading -> ColorStateList.valueOf(Color.BLUE)
                    AppAsyncImageLoadingStatus.Error -> ColorStateList.valueOf(Color.RED)
                    else -> null
                }.also {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.imageTintList = it
                    }
                }
            }

        lifecycleScope.launchWhenResumed {
            viewBinding.appImageDownload.loadImage(
                uri = Uri.parse("http://lproweb.procempa.com.br/pmpa/prefpoa/seda_news/usu_img/Papel%20de%20Parede.png"),
                //size = AppAsyncImageViewDefaults.ThumbnailSize
            )
        }
    }
}