package com.example.catastrophic.ui.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.catastrophic.R
import com.example.catastrophic.databinding.FragmentImageBinding
import com.example.catastrophic.utils.loadingDrawable
import org.koin.androidx.viewmodel.ext.android.viewModel

/** TODO: A fragment for displying an image. */
class ImageFragment : Fragment() {

    var transitionId = ""
    var imageUrl = ""

    private var _binding: FragmentImageBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        private const val KEY_TRANSITION_ID = "com.example.catastrophic.key.transitionId"
        private const val KEY_IMAGE_URL = "com.example.catastrophic.key.imageUrl"

        fun newInstance(transitionId: String, imageUrl: String): ImageFragment {
            val fragment = ImageFragment()

            val args = Bundle()
            args.putString(KEY_TRANSITION_ID, transitionId)
            args.putString(KEY_IMAGE_URL, imageUrl)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = requireArguments()
        binding.image.transitionName = args.getString(KEY_TRANSITION_ID)!!
        imageUrl = args.getString(KEY_IMAGE_URL)!!
        Glide.with(this)
            .load(imageUrl)
            .placeholder(loadingDrawable(requireContext()))
            .error(R.drawable.ic_baseline_error_36)
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.image.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    parentFragment?.startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    parentFragment?.startPostponedEnterTransition()
                    return false
                }

            })
            .into(binding.image)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}