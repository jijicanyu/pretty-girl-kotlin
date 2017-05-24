package me.zsj.pretty_girl_kotlin.utils

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast

/**
 * @author zsj
 */


inline fun Context.short(message: String?) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

inline fun Context.short(@StringRes stringId: Int) =
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()

inline fun Context.long(message: String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

inline fun Context.long(@StringRes stringId: Int) =
        Toast.makeText(this, getString(stringId), Toast.LENGTH_LONG).show()
