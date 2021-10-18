package com.example.catastrophic.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

fun transitionId(position: Int) = "image_transition_$position"

fun loadingDrawable(context: Context): Drawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 5f
        centerRadius = 30f
        setColorSchemeColors(0xffcccccc.toInt())
        start()
    }
}