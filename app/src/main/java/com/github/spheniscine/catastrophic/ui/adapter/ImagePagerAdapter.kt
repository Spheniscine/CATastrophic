package com.github.spheniscine.catastrophic.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.github.spheniscine.catastrophic.repository.CatProvider
import com.github.spheniscine.catastrophic.ui.fragment.ImageFragment
import com.github.spheniscine.catastrophic.utils.transitionId

// Using deprecated pager adapter, as doing the recommended upgrade breaks transitions
class ImagePagerAdapter(fragment: Fragment, private val catProvider: CatProvider):
    FragmentStatePagerAdapter(fragment.childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return catProvider.numCats
    }

    override fun getItem(position: Int): Fragment {
        return ImageFragment.newInstance(transitionId(position), position)
    }

}