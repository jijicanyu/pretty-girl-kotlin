package me.zsj.pretty_girl_kotlin.utils

import android.content.Context
import android.support.annotation.IntegerRes

/**
 * @author zsj
 */

inline fun Context.dimensSize(@IntegerRes dimensId: Int) = resources.getDimensionPixelSize(dimensId)