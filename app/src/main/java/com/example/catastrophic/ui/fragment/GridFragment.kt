package com.example.catastrophic.ui.fragment

import android.os.Bundle
import androidx.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.lifecycle.Observer
import com.example.catastrophic.MainViewModel
import com.example.catastrophic.R
import com.example.catastrophic.ui.adapter.GridAdapter
import com.example.catastrophic.databinding.FragmentGridBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.logging.Logger

/** A fragment for displying a grid of images. */
class GridFragment : Fragment() {

    companion object {
        fun newInstance() = GridFragment()
    }

    private var _binding: FragmentGridBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by sharedViewModel()

    private val gridAdapter by lazy { GridAdapter(this, mainViewModel) }

    private var scaleGestureDetector: ScaleGestureDetector? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGridBinding.inflate(inflater, container, false)

        binding.recyclerView.adapter = gridAdapter

        prepareTransitions()
        postponeEnterTransition()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scrollToPosition()

        scaleGestureDetector = ScaleGestureDetector(context,
            object: ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    mainViewModel.updateScaleFactor(detector.scaleFactor)
                    return false
                }
            })

        binding.recyclerView.setOnTouchListener { _, event ->
            scaleGestureDetector!!.onTouchEvent(event)
        }

        mainViewModel.scaleFactorLd.observe(viewLifecycleOwner) { scaleFactor ->
            binding.root.scaleX = scaleFactor
            binding.root.scaleY = scaleFactor
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Scrolls the recycler view to show the last viewed item in the grid. This is important when
     * navigating back from the grid.
     */
    private fun scrollToPosition() {
        binding.recyclerView.addOnLayoutChangeListener(
            object: View.OnLayoutChangeListener {
                override fun onLayoutChange(
                    v: View?,
                    left: Int,
                    top: Int,
                    right: Int,
                    bottom: Int,
                    oldLeft: Int,
                    oldTop: Int,
                    oldRight: Int,
                    oldBottom: Int
                ) {
                    binding.recyclerView.removeOnLayoutChangeListener(this)
                    if(!mainViewModel.shouldScroll) return
                    val layoutManager = binding.recyclerView.layoutManager ?: return
                    val viewAtPosition = layoutManager.findViewByPosition(mainViewModel.currentPosition)
                    // Scroll to position if the view for the current position is null (not currently part of
                    // layout manager children), or it's not completely visible.
                    if(viewAtPosition == null || layoutManager
                            .isViewPartiallyVisible(viewAtPosition, false, true)) {
                        binding.recyclerView.post {
                            layoutManager.scrollToPosition(mainViewModel.currentPosition)
                        }
                    }
                    mainViewModel.shouldScroll = false
                }
            }
        )
    }

    /**
     * Prepares the shared element transition to the pager fragment, as well as the other transitions
     * that affect the flow.
     */
    private fun prepareTransitions() {
        val context = context ?: return
        exitTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.grid_exit_transition)

        // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
        setExitSharedElementCallback(
            object: SharedElementCallback() {
                override fun onMapSharedElements(
                    names: MutableList<String>,
                    sharedElements: MutableMap<String, View>
                ) {
                    // Locate the ViewHolder for the clicked position.
                    val selectedViewHolder = binding.recyclerView
                        .findViewHolderForAdapterPosition(mainViewModel.currentPosition) ?: return

                    // Map the first shared element name to the child ImageView.
                    sharedElements[names[0]] = selectedViewHolder.itemView.findViewById(R.id.item_image)
                }
            }
        )
    }
}