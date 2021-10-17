package com.example.catastrophic.ui.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.catastrophic.R
import com.example.catastrophic.databinding.ItemImageBinding
import com.example.catastrophic.utils.dp
import com.example.catastrophic.utils.scaledDrawable
import kotlin.math.roundToInt

/** A RecyclerView adapter for displaying a grid of images. */
class GridAdapter(val fragment: Fragment): RecyclerView.Adapter<GridAdapter.ImageViewHolder>() {

    var urls: List<String> = emptyList()
    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    val requestManager = Glide.with(fragment)
    val context get() = fragment.requireContext()

    fun loadingDrawable(): Drawable {
        return CircularProgressDrawable(context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            setColorSchemeColors(0xffcccccc.toInt())
            start()
        }
    }

    fun errorDrawable(): Drawable {
        return AppCompatResources.getDrawable(context, R.drawable.ic_error) ?: error("Can't load error drawable")
     }


    inner class ImageViewHolder(itemView: View, val binding: ItemImageBinding):
        RecyclerView.ViewHolder(itemView) {

        fun onBind() {
            setImage()
        }

        fun setImage() {
            requestManager
                .load(urls[adapterPosition])
                .placeholder(loadingDrawable())
                .error(errorDrawable())
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