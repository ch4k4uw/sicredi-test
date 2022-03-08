package com.sicredi.instacredi.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sicredi.instacredi.databinding.FragmentBottomSheetProfileBinding
import com.sicredi.presenter.event.EventDetailsViewModel
import com.sicredi.instacredi.feed.FeedFragment
import com.sicredi.presenter.feed.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var viewBinding: FragmentBottomSheetProfileBinding

    private val logoutIntent: () -> Unit by lazy {
        if (parentFragment is FeedFragment) {
            viewModels<FeedViewModel>({requireParentFragment()}).value.let {
                {
                    it.logout()
                }
            }
        } else {
            viewModels<EventDetailsViewModel>({requireParentFragment()}).value.let {
                {
                    it.logout()
                }
            }
        }
    }

    private val name: String
        get() = arguments?.getString(ProfileBottomSheetConstants.Key.Name) ?: ""

    private val email: String
        get() = arguments?.getString(ProfileBottomSheetConstants.Key.Email) ?: ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentBottomSheetProfileBinding.inflate(inflater, container, false).let {
        it.root.apply { viewBinding = it }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListeners()
    }

    private fun setupView() {
        viewBinding.profileUserName.text = name
        viewBinding.profileUserEmail.text = email
    }

    private fun setupListeners() {
        viewBinding.logoutAction.setOnClickListener {
            dismiss()
            logoutIntent()
        }
    }
}

fun FragmentManager.showProfileBottomSheetFragment(name: String, email: String) {
    ProfileBottomSheetFragment().also { dialog ->
        dialog.arguments = bundleOf(
            ProfileBottomSheetConstants.Key.Name to name,
            ProfileBottomSheetConstants.Key.Email to email,
        )
    }.show(this, ProfileBottomSheetFragment::class.simpleName!!)
}