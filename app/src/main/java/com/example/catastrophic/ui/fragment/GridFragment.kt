package com.example.catastrophic.ui.fragment

import android.os.Bundle
import androidx.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
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
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by sharedViewModel()

    private val gridAdapter by lazy { GridAdapter(this, mainViewModel::currentPosition) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGridBinding.inflate(inflater, container, false)

        binding.recyclerView.adapter = gridAdapter

        prepareTransitions()
        //postponeEnterTransition() // TODO: this transition isn't working right

        mainViewModel.catData.observe(viewLifecycleOwner) { cats ->
            gridAdapter.urls = cats.orEmpty().map { it.url }
            //Log.d("GridFragment", "urls: ${gridAdapter.urls}")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scrollToPosition()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

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
                }
            }
        )
    }

    private fun prepareTransitions() {
        exitTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.grid_exit_transition)

        setExitSharedElementCallback(
            object: SharedElementCallback() {
                override fun onMapSharedElements(
                    names: MutableList<String>,
                    sharedElements: MutableMap<String, View>
                ) {
                    val selectedViewHolder = binding.recyclerView
                        .findViewHolderForAdapterPosition(mainViewModel.currentPosition) ?: return

                    sharedElements[names[0]] = selectedViewHolder.itemView.findViewById(R.id.item_image)
                }
            }
        )
    }
}