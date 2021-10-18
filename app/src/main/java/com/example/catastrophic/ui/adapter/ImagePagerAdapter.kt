package com.example.catastrophic.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.catastrophic.repository.data.CatData
import com.example.catastrophic.ui.fragment.ImageFragment
import com.example.catastrophic.utils.transitionId

class ImagePagerAdapter(fragment: Fragment, val catData: List<CatData>):
    FragmentStatePagerAdapter(fragment.childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return catData.size
    }

    override fun getItem(position: Int): Fragment {
        return ImageFragment.newInstance(transitionId(position), catData[position].url)
    }

}