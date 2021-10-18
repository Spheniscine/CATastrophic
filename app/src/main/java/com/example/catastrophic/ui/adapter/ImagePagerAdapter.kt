package com.example.catastrophic.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.catastrophic.repository.data.CatData
import com.example.catastrophic.ui.fragment.ImageFragment
import com.example.catastrophic.utils.transitionId

class ImagePagerAdapter(fragment: Fragment, val catData: List<CatData>):
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return catData.size
    }

    override fun createFragment(position: Int): Fragment {
        return ImageFragment.newInstance(transitionId(position), catData[position].url)
    }

}