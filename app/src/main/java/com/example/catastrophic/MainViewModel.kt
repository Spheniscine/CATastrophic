package com.example.catastrophic

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catastrophic.R
import com.example.catastrophic.repository.CatApiRepository
import com.example.catastrophic.repository.CatProvider
import com.example.catastrophic.repository.CatRepository
import com.example.catastrophic.repository.data.CatData
import com.example.catastrophic.utils.ContextValue
import com.example.catastrophic.utils.DrawableId
import kotlinx.coroutines.launch

class MainViewModel(private val catRepository: CatRepository) : ViewModel(), CatProvider by catRepository {

//    private val _currentPosition = MutableLiveData<Int>(0)
//    val currentPositionLD: LiveData<Int> get() = _currentPosition
//    var currentPosition get() = _currentPosition.value ?: 0
//        set(value) { _currentPosition.value = value }
    var currentPosition = 0

//    // mockup
//    fun getCats(): List<ContextValue<Drawable?>> {
//        return List(18) { DrawableId(R.drawable.cat_api_example_0) }
//    }
}