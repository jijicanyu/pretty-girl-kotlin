package me.zsj.pretty_girl_kotlin.utils

import android.content.Context
import android.content.res.Configuration

/**
 * @author zsj
 */
class ConfigUtils {

    companion object {

        @JvmStatic
        fun isOrientationPortrait(context: Context): Boolean {
            if (context.resources.configuration.orientation ==
                    Configuration.ORIENTATION_PORTRAIT) {
                return true
            }
            return false
        }

        @JvmStatic
        fun isOrientationLandscape(context: Context): Boolean {
            if (context.resources.configuration.orientation ==
                    Configuration.ORIENTATION_LANDSCAPE) {
                return true
            }
            return false
        }

    }

}