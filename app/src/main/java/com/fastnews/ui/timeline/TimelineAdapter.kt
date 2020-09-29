package com.fastnews.ui.timeline

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.fastnews.R
import com.fastnews.service.model.PostData
import kotlinx.android.synthetic.main.include_item_timeline_thumbnail.view.*

class TimelineAdapter(val onClickItem: (PostData, ImageView) -> Unit) : RecyclerView.Adapter<TimelineItemViewHolder>() {

    private var items: MutableList<PostData> = mutableListOf()

    fun setData(items: List<PostData>) {
        val size = this.items.size
        this.items.addAll(items)
        val sizeNew = this.items.size
        notifyItemRangeChanged(size, sizeNew)
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineItemViewHolder
            = TimelineItemViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_timeline,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: TimelineItemViewHolder, position: Int) {
        holder.data = items[position]
        holder.view.setOnClickListener { onClickItem(items[position], holder.view.item_timeline_thumbnail) }
    }
}