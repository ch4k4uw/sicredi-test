package com.sicredi.instacredi

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.*

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
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