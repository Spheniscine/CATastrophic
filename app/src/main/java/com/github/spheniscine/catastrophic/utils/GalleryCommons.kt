package com.github.spheniscine.catastrophic.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

/** generates transition id string, keyed by position */
fun transitionId(position: Int) = "image_transition_$position"

/** generates animation when loading an image */
fun loadingDrawable(context: Context): Drawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 5f
        centerRadius = 30f
        setColorSchemeColors(0xffcccccc.toInt())
        start()
    }
}