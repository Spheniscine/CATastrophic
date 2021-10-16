package com.example.catastrophic.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.example.catastrophic.R
import com.example.catastrophic.adapter.GridAdapter
import com.example.catastrophic.databinding.FragmentGridBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GridFragment : Fragment() {

    companion object {
        fun newInstance() = GridFragment()
    }

    private var _binding: FragmentGridBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cats = mainViewModel.getCats().map {
            it.reify(requireContext())!!
        }
        binding.recyclerView.adapter = GridAdapter(this, cats)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}