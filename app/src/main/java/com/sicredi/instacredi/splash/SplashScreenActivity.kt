package com.sicredi.instacredi.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sicredi.instacredi.databinding.ActivitySplashScreenBinding
import com.sicredi.instacredi.signup.SignUpViewModel
import com.sicredi.instacredi.splash.interaction.SplashScreenState
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private val viewModel by viewModels<SplashScreenViewModel>()
    private val suViewModel by viewModels<SignUpViewModel>()
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
            is SplashScreenState.ShowFeedScreen -> {
                val action = intent.action
                val data = intent.data
                val eventId = data?.getQueryParameter("eventId")
                Timber.i(
                    "Required Feed screen display.\nAction: $action\ndata: $data\n" +
                            "eventId: $eventId"
                )
            }
            SplashScreenState.ShowSignInScreen -> {
                Timber.i("Required Sign-In screen display.")
                suViewModel.signUp(
                    name = "Pedro Motta",
                    email = "pedro.motta@avenuecode.com",
                    password = "12345678"
                )
            }
        }
    }

}