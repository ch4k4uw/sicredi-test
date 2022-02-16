package com.sicredi.instacredi

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.*

@HiltAndroidApp
class App : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
            MultiDex.install(this)
        }
    }

    override fun onCreate() {
        super.onCreate()
        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return String.format(
                        Locale.getDefault(),
                        "(%s:%s)",
                        element.fileName,
                        element.lineNumber
                    )
                }
            })
        } else {
            Timber.plant(object : Timber.DebugTree() {
                override fun isLoggable(tag: String?, priority: Int): Boolean {
                    return priority == Log.WARN || priority == Log.ERROR
                }
            })
        }
    }
}