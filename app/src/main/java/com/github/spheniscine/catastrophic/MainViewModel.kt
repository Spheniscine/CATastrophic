package com.github.spheniscine.catastrophic

import androidx.lifecycle.ViewModel
import com.github.spheniscine.catastrophic.repository.CatProvider
import com.github.spheniscine.catastrophic.repository.CatRepository

class MainViewModel(private val catRepository: CatRepository) : ViewModel(), CatProvider by catRepository {

    /** stores image position that view pager is looking at, used to scroll to correct position when going back to grid */
    var currentPosition = 0

    /** whether grid recycler view should scroll */
    var shouldScroll = false
}