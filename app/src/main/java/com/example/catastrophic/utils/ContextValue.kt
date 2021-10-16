package com.example.catastrophic.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.example.catastrophic.R

/**
 * Interface for values that need an Android context to "reify" to a more usable value, e.g. resource ids
 * This is useful for emitting from or passing around among viewmodels, for better unit-testability
 */
interface ContextValue<T> {
    fun reify(context: Context): T
}

data class DrawableId(@DrawableRes val resId: Int): ContextValue<Drawable?> {
    override fun reify(context: Context): Drawable? {
        return AppCompatResources.getDrawable(context, resId)
    }
}