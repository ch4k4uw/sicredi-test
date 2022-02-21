package com.sicredi.instacredi.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sicredi.instacredi.databinding.ActivitySplashScreenBinding
import com.sicredi.instacredi.splash.interaction.SplashScreenState
import com.sicredi.instacredi.startMainActivityForEventDetails
import com.sicredi.instacredi.startMainActivityForFeedFragment
import com.sicredi.instacredi.startMainActivityForSignInFragment
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private val viewModel by viewModels<SplashScreenViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivitySplashScreenBinding.inflate(layoutInflater).root)
        lifecycleScope.launchWhenResumed {
            viewModel.state.observe(this@SplashScreenActivity, ::handleState)
        }
    }

    private fun handleState(state: SplashScreenState) {
        when (state) {
            is SplashScreenState.NotInitialized -> throw state.cause
            is SplashScreenState.EventDetailsNotLoaded -> throw state.cause
            is SplashScreenState.ShowFeedScreen -> {
                val data = intent.data
                val eventId = data?.getQueryParameter("eventId")
                if (eventId != null) {
                    viewModel.findDetails(eventId = eventId)
                } else {
                    startMainActivityForFeedFragment(user = state.user)
                }
            }
            SplashScreenState.ShowSignInScreen -> {
                startMainActivityForSignInFragment()
            }
            is SplashScreenState.EventDetailsSuccessfulLoaded -> {
                startMainActivityForEventDetails(eventDetails = state.eventDetails)
            }
        }
    }

}