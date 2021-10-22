package com.github.spheniscine.catastrophic.ui.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.spheniscine.catastrophic.BuildConfig
import com.github.spheniscine.catastrophic.MainViewModel
import com.github.spheniscine.catastrophic.R
import com.github.spheniscine.catastrophic.databinding.FragmentImageBinding
import com.github.spheniscine.catastrophic.repository.CatProvider
import com.github.spheniscine.catastrophic.utils.loadingDrawable
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/** A fragment for displying an image. */
class ImageFragment : Fragment() {

    var position = 0

    private var _binding: FragmentImageBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    val catProvider: CatProvider by sharedViewModel<MainViewModel>()

    companion object {
        private const val KEY_TRANSITION_ID = "${BuildConfig.APPLICATION_ID}.key.transitionId"
        private const val KEY_POSITION = "${BuildConfig.APPLICATION_ID}.key.position"

        fun newInstance(transitionId: String, position: Int): ImageFragment {
            val fragment = ImageFragment()

            val args = Bundle()
            args.putString(KEY_TRANSITION_ID, transitionId)
            args.putInt(KEY_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)

        // Just like we do when binding views at the grid, we set the transition name to be the string
        // value of the image res.
        val args = requireArguments()
        binding.image.transitionName = args.getString(KEY_TRANSITION_ID)!!
        position = args.getInt(KEY_POSITION)

        lifecycleScope.launch {
            val context = context ?: return@launch
            binding.image.scaleType = ImageView.ScaleType.FIT_CENTER
            Glide.with(this@ImageFragment).load(loadingDrawable(context)).into(binding.image)
            parentFragment?.startPostponedEnterTransition()
            val catData = catProvider.getCatData(position)

            Glide.with(this@ImageFragment)
                .load(catData?.url)
                .placeholder(loadingDrawable(context))
                .error(R.drawable.ic_baseline_error_36)
                .listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.image.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        //parentFragment?.startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        //parentFragment?.startPostponedEnterTransition()
                        return false
                    }

                })
                .into(binding.image)
        }



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}