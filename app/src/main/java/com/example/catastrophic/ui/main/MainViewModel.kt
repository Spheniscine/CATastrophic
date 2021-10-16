package com.example.catastrophic.ui.main

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import com.example.catastrophic.R
import com.example.catastrophic.utils.ContextValue
import com.example.catastrophic.utils.DrawableId

class MainViewModel : ViewModel() {

    // mockup
    fun getCats(): List<ContextValue<Drawable?>> {
        return List(18) { DrawableId(R.drawable.cat_api_example_0) }
    }
}