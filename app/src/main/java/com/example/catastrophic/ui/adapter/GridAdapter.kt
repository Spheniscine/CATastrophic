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
import androidx.transition.TransitionSet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.catastrophic.R
import com.example.catastrophic.databinding.ItemImageBinding
import com.example.catastrophic.ui.fragment.ImagePagerFragment
import com.example.catastrophic.utils.dp
import com.example.catastrophic.utils.scaledDrawable
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.roundToInt
import kotlin.reflect.KMutableProperty0

/** A RecyclerView adapter for displaying a grid of images. */
class GridAdapter(val fragment: Fragment, val currentPosition: KMutableProperty0<Int>): RecyclerView.Adapter<GridAdapter.ImageViewHolder>() {

    interface ViewHolderListener {
        fun onLoadCompleted(imageView: ImageView, adapterPosition: Int)
        fun onItemClicked(view: View, imageView: ImageView, adapterPosition: Int)
    }

    var urls: List<String> = emptyList()
    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    val requestManager = Glide.with(fragment)
    val context get() = fragment.requireContext()
    val viewHolderListener = object : ViewHolderListener {
        val enterTransitionStarted = AtomicBoolean()

        override fun onLoadCompleted(imageView: ImageView, adapterPosition: Int) {
            if(currentPosition.get() != adapterPosition) return
            if(enterTransitionStarted.getAndSet(true)) return
            fragment.startPostponedEnterTransition()
        }

        override fun onItemClicked(view: View, imageView: ImageView, adapterPosition: Int) {
            currentPosition.set(adapterPosition)
            (fragment.exitTransition as TransitionSet).excludeTarget(view, true)
            val transitioningView = imageView
            fragment.parentFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true) // Optimize for shared element transition
                .addSharedElement(transitioningView, transitioningView.transitionName)
                .replace(R.id.container, ImagePagerFragment(), ImagePagerFragment::class.simpleName)
                .addToBackStack(null)
                .commit()
        }
    }

    fun loadingDrawable(): Drawable {
        return CircularProgressDrawable(context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            setColorSchemeColors(0xffcccccc.toInt())
            start()
        }
    }


    inner class ImageViewHolder(itemView: View,
                                val image: ImageView,
                                val viewHolderListener: ViewHolderListener):
        RecyclerView.ViewHolder(itemView) {

        init {
            image.setOnClickListener { view ->
                viewHolderListener.onItemClicked(view, image, adapterPosition)
            }
        }

        /**
         * Binds this view holder to the given adapter position.
         *
         * The binding will load the image into the image view, as well as set its transition name for
         * later.
         */
        fun onBind() {
            //if (adapterPosition >= urls.size) return
            setImage()
            image.transitionName = urls[adapterPosition]
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
                        image.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        viewHolderListener.onLoadCompleted(image, adapterPosition)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        viewHolderListener.onLoadCompleted(image, adapterPosition)
                        return false
                    }

                })
                .into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view = binding.root
        return ImageViewHolder(view, binding.itemImage, viewHolderListener)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int {
        return urls.size
    }
}