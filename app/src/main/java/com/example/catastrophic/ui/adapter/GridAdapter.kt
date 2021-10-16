package com.example.catastrophic.ui.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.catastrophic.databinding.ItemImageBinding

/** A RecyclerView adapter for displaying a grid of images. */
class GridAdapter(fragment: Fragment): RecyclerView.Adapter<GridAdapter.ImageViewHolder>() {

    var urls: List<String> = emptyList()
    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    val requestManager = Glide.with(fragment)


    inner class ImageViewHolder(itemView: View, val binding: ItemImageBinding):
        RecyclerView.ViewHolder(itemView) {

        fun onBind() {
            setImage()
        }

        fun setImage() {
            requestManager
                .load(urls[adapterPosition])
                .into(binding.itemImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view = binding.root
        return ImageViewHolder(view, binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int {
        return urls.size
    }
}