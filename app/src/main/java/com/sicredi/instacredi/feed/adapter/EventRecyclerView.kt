package com.sicredi.instacredi.feed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sicredi.instacredi.databinding.ListitemFeedBinding
import com.sicredi.presenter.feed.interaction.EventHeadView

class EventRecyclerView(
    private val events: List<EventHeadView>,
    private val onEventHeadClick: (EventHeadView) -> Unit,
) : RecyclerView.Adapter<EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder =
        ListitemFeedBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .run {
                EventViewHolder(viewBinding = this) { position ->
                    onEventHeadClick(events[position])
                }
            }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size
}