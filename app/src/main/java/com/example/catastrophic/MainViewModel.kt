package com.example.catastrophic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.catastrophic.repository.CatProvider
import com.example.catastrophic.repository.CatRepository
import com.example.catastrophic.repository.CatRepositoryImpl

class MainViewModel(private val catRepository: CatRepository) : ViewModel(), CatProvider by catRepository {

    /** stores image position that view pager is looking at, used to scroll to correct position when going back to grid */
    var currentPosition = 0

    /** whether grid recycler view should scroll */
    var shouldScroll = false

    /** stores scale factor for pinch-to-zoom feature */
    private val scaleFactorMld = MutableLiveData<Float>(1.0f)
    val scaleFactorLd = scaleFactorMld as LiveData<Float>
    var scaleFactor get() = scaleFactorMld.value!!
        private set(value) { scaleFactorMld.value = value }

    fun updateScaleFactor(factor: Float) {
        scaleFactor = (scaleFactor * factor).coerceIn(1.0f, 10.0f)
    }

    fun resetScaleFactor() { scaleFactor = 1.0f }
}