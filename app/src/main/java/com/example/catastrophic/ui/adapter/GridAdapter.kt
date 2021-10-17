package com.example.catastrophic.ui.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
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


    inner class ImageViewHolder(itemView: View, val binding: ItemImageBinding):
        RecyclerView.ViewHolder(itemView) {

        fun onBind() {
            setImage()
        }

        fun setImage() {
            requestManager
                .load(urls[adapterPosition])
                .placeholder(loadingDrawable())
                .error(R.drawable.ic_baseline_error_36)
                .listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.itemImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                })
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