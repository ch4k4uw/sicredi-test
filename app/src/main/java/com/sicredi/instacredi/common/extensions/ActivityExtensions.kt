package com.sicredi.instacredi.common.extensions

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment

fun AppCompatActivity.navHostFragment(@IdRes id: Int): Lazy<NavHostFragment> = lazy {
    supportFragmentManager.findFragmentById(id) as NavHostFragment
}