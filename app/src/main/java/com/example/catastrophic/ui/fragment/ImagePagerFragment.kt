package com.example.catastrophic.ui.fragment

import android.os.Bundle
import androidx.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.viewpager2.widget.ViewPager2
import com.example.catastrophic.MainViewModel
import com.example.catastrophic.R
import com.example.catastrophic.databinding.FragmentImageBinding
import com.example.catastrophic.databinding.FragmentImagePagerBinding
import com.example.catastrophic.ui.adapter.ImagePagerAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ImagePagerFragment : Fragment() {
    private var _binding: FragmentImagePagerBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by sharedViewModel()

    private val adapter by lazy { ImagePagerAdapter(this, mainViewModel.catData.value.orEmpty()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImagePagerBinding.inflate(inflater, container, false)

        val viewPager = binding.viewPager
        viewPager.adapter = adapter
        viewPager.currentItem = mainViewModel.currentPosition
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mainViewModel.currentPosition = position
            }
        })

        prepareSharedElementTransition()

        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
        if (savedInstanceState == null) {
            postponeEnterTransition()
        }

        return binding.root
    }

    private fun prepareSharedElementTransition() {
        val transition =
            TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition

        setEnterSharedElementCallback(object: SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
                super.onMapSharedElements(names, sharedElements)
                val currentFragment = adapter.createFragment(mainViewModel.currentPosition)
                val view = currentFragment.view ?: return
                sharedElements[names[0]] = view.findViewById(R.id.item_image)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}