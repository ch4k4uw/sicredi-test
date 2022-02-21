package com.sicredi.instacredi.feed.adapter

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.sicredi.core.extensions.asFormattedPrice
import com.sicredi.core.ui.component.AppAsyncImageViewDefaults
import com.sicredi.instacredi.databinding.ListitemFeedBinding
import com.sicredi.instacredi.feed.interaction.EventHeadView

class EventViewHolder(
    private val viewBinding: ListitemFeedBinding,
    private val onEventClick: (Int) -> Unit
) : RecyclerView.ViewHolder(viewBinding.root) {
    init { viewBinding.root.setOnClickListener { onEventClick(adapterPosition) } }
    fun bind(event: EventHeadView) {
        viewBinding.image.isFocusable = false
        viewBinding.image.isClickable = false
        viewBinding.image.loadImage(
            uri = Uri.parse(event.image), size = AppAsyncImageViewDefaults.ThumbnailSize
        )
        viewBinding.title.text = event.title
        viewBinding.price.text = event.price.asFormattedPrice
    }
}