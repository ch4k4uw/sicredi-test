package com.sicredi.instacredi.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sicredi.core.R
import com.sicredi.instacredi.databinding.FragmentBottomSheetEventOptionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventOptionsBottomSheetFragment : BottomSheetDialogFragment() {
    private val viewModel by viewModels<EventDetailsViewModel>({ requireParentFragment() })
    private lateinit var viewBinding: FragmentBottomSheetEventOptionsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentBottomSheetEventOptionsBinding.inflate(inflater, container, false).let {
        it.root.apply { viewBinding = it }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.checkInActionLayout.setOnClickListener { viewModel.performCheckIn(); dismiss() }
        viewBinding.shareActionLayout.setOnClickListener { viewModel.shareEvent(); dismiss() }
    }
}

fun FragmentManager.showEventOptionsBottomSheetFragment() {
    EventOptionsBottomSheetFragment().show(
        this, EventOptionsBottomSheetFragment::class.simpleName!!
    )
}