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
import com.example.catastrophic.MainViewModel
import com.example.catastrophic.R
import com.example.catastrophic.databinding.ItemImageBinding
import com.example.catastrophic.repository.CatProvider
import com.example.catastrophic.ui.fragment.ImagePagerFragment
import com.example.catastrophic.utils.dp
import com.example.catastrophic.utils.loadingDrawable
import com.example.catastrophic.utils.scaledDrawable
import com.example.catastrophic.utils.transitionId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.roundToInt
import kotlin.reflect.KMutableProperty0

/** A RecyclerView adapter for displaying a grid of images. */
class GridAdapter(private val fragment: Fragment, private val mainViewModel: MainViewModel): RecyclerView.Adapter<GridAdapter.ImageViewHolder>() {

    private lateinit var coroutineScope: CoroutineScope

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        coroutineScope = MainScope()
    }

    // note: should requests be canceled? Could that cause problems?
//    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
//        coroutineScope.cancel()
//    }

    interface ViewHolderListener {
        fun onLoadCompleted(imageView: ImageView, adapterPosition: Int)
        fun onItemClicked(view: View, imageView: ImageView, adapterPosition: Int)
    }

    val requestManager = Glide.with(fragment)
    val context get() = fragment.requireContext()
    inner class ViewHolderListenerImpl : ViewHolderListener {
        val enterTransitionStarted = AtomicBoolean()

        override fun onLoadCompleted(imageView: ImageView, adapterPosition: Int) {
            if(mainViewModel.currentPosition != adapterPosition) return
            if(enterTransitionStarted.getAndSet(true)) return
            fragment.startPostponedEnterTransition()
        }

        override fun onItemClicked(view: View, imageView: ImageView, adapterPosition: Int) {
            mainViewModel.currentPosition = adapterPosition
            mainViewModel.shouldScroll = true
            (fragment.exitTransition as TransitionSet).excludeTarget(view, true)
            val transitioningView = imageView
            fragment.requireActivity().supportFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true) // Optimize for shared element transition
                .addSharedElement(transitioningView, transitioningView.transitionName)
                .replace(R.id.container, ImagePagerFragment(), ImagePagerFragment::class.simpleName)
                .addToBackStack(null)
                .commit()
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
            image.transitionName = transitionId(adapterPosition)
        }

        fun setImage() {
            coroutineScope.launch {
                image.scaleType = ImageView.ScaleType.CENTER_CROP
                requestManager.load(loadingDrawable(context)).into(image)
                val catData = mainViewModel.getCatData(adapterPosition)
                requestManager.load(catData?.url)
                    .placeholder(loadingDrawable(context))
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view = binding.root
        return ImageViewHolder(view, binding.itemImage, ViewHolderListenerImpl())
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int {
        return mainViewModel.numCats
    }
}