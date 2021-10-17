package com.example.catastrophic.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import kotlin.math.roundToInt

fun Context.scaledDrawable(@DrawableRes id: Int, width: Float, height: Float): Drawable? {
    return AppCompatResources.getDrawable(this, id)
        ?.toBitmap(width.roundToInt(), height.roundToInt())
        ?.toDrawable(resources)
}

/** scales a dp value to pixels */
fun Context.dp(value: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)