package com.example.catastrophic.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.catastrophic.R

class GridAdapter(fragment: Fragment, val drawables: List<Drawable>): RecyclerView.Adapter<GridAdapter.ImageViewHolder>() {

    val requestManager = Glide.with(fragment)


    inner class ImageViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView) {

        val imageView = itemView.findViewById<ImageView>(R.id.item_image) ?:
            error("ImageViewHolder requires inner ImageView with item_image id")
        // TODO: on click listener

        fun onBind() {
            setImage()
        }

        fun setImage() {
            requestManager
                .load(drawables[adapterPosition])
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int {
        return drawables.size
    }
}