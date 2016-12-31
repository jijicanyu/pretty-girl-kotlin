package me.zsj.pretty_girl_kotlin.utils

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

/**
 * @author zsj
 */
class ScreenUtils {

    companion object {

        @JvmStatic
        fun getWidth(context: Context) : Int {
            val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val size = Point()
            val display = manager.defaultDisplay
            display.getSize(size)
            return size.x
        }

        @JvmStatic
        fun getHeight(context: Context) : Int {
            val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val size = Point()
            val display = manager.defaultDisplay
            display.getSize(size)
            return size.y
        }

    }

}