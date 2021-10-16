package com.example.catastrophic.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.catastrophic.MainViewModel
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

    private val gridAdapter by lazy { GridAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = gridAdapter

        mainViewModel.catData.observe(viewLifecycleOwner) { cats ->
            gridAdapter.urls = cats.orEmpty().map { it.url }
            //Log.d("GridFragment", "urls: ${gridAdapter.urls}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}