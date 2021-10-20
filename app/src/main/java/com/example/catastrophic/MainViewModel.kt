package com.example.catastrophic

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catastrophic.R
import com.example.catastrophic.repository.CatProvider
import com.example.catastrophic.repository.CatRepository
import com.example.catastrophic.repository.data.CatData
import com.example.catastrophic.utils.ContextValue
import com.example.catastrophic.utils.DrawableId
import kotlinx.coroutines.launch

class MainViewModel(private val catRepository: CatRepository) : ViewModel(), CatProvider by catRepository {

    /** stores image position that view pager is looking at, used to scroll to correct position when going back to grid */
    var currentPosition = 0

    /** whether grid recycler view should scroll */
    var shouldScroll = false
}