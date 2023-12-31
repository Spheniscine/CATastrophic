package com.github.spheniscine.catastrophic.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.transition.TransitionInflater
import androidx.fragment.app.Fragment
import androidx.core.app.SharedElementCallback
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.github.spheniscine.catastrophic.MainViewModel
import com.github.spheniscine.catastrophic.R
import com.github.spheniscine.catastrophic.databinding.FragmentImagePagerBinding
import com.github.spheniscine.catastrophic.ui.adapter.ImagePagerAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ImagePagerFragment : Fragment() {
    private var _binding: FragmentImagePagerBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by sharedViewModel()

    private val adapter by lazy { ImagePagerAdapter(this, mainViewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagePagerBinding.inflate(inflater, container, false)

        val viewPager = binding.viewPager
        viewPager.adapter = adapter
        // Set the current position and add a listener that will update the selection coordinator when
        // paging the images.
        viewPager.currentItem = mainViewModel.currentPosition
        viewPager.addOnPageChangeListener(object: ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mainViewModel.currentPosition = position
            }
        })

        prepareSharedElementTransition()

        // Avoid a postponeEnterTransition on orientation change, and postpone only on first creation.
        if (savedInstanceState == null) {
            postponeEnterTransition()
        }

        return binding.root
    }

    private fun prepareSharedElementTransition() {
        val context = context ?: return
        val transition =
            TransitionInflater.from(context)
                .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition

        // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
        setEnterSharedElementCallback(object: SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
                // Locate the image view at the primary fragment (the ImageFragment that is currently
                // visible). To locate the fragment, call instantiateItem with the selection position.
                // At this stage, the method will simply return the fragment at the position and will
                // not create a new one.
                super.onMapSharedElements(names, sharedElements)
                val currentFragment =
                    adapter.instantiateItem(binding.viewPager, mainViewModel.currentPosition) as Fragment
                val view = currentFragment.view ?: return

                // Map the first shared element name to the child ImageView.
                sharedElements[names[0]] = view.findViewById(R.id.image)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_image_pager, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share -> {
                lifecycleScope.launch {
                    val url = mainViewModel.getShareUrl()
                    if (url != null) {
                        val shareIntent = with(Intent(Intent.ACTION_SEND)) {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, url)
                        }
                        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_with)))
                    }
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}