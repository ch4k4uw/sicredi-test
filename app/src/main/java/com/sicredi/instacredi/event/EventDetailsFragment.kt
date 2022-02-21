package com.sicredi.instacredi.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sicredi.instacredi.databinding.FragmentEventDetailsBinding

class EventDetailsFragment : Fragment() {
    private lateinit var viewBinding: FragmentEventDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentEventDetailsBinding.inflate(inflater, container, false).let {
        it.root.apply { viewBinding = it }
    }
}