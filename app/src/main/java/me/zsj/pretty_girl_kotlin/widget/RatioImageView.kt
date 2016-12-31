package me.zsj.pretty_girl_kotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * @author zsj
 */
class RatioImageView : ImageView {

    private var originalWidth: Int = 0
    private var originalHeight: Int = 0


    constructor(context: Context) : this(context, null)

    constructor(context: Context, set: AttributeSet?) : this(context, set, 0)

    constructor(context: Context, set: AttributeSet?, defAttr: Int) :
            super(context, set, defAttr)

    fun setOriginalWidth(originalWidth: Int) {
        this.originalWidth = originalWidth
    }

    fun setOriginalHeight(originalHeight: Int) {
        this.originalHeight = originalHeight
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (originalWidth > 0 && originalHeight > 0) {
            val ratio = originalWidth.toFloat() / originalHeight.toFloat()

            val width = MeasureSpec.getSize(widthMeasureSpec)
            var height = MeasureSpec.getSize(heightMeasureSpec)

            if (width > 0) {
                height = (width.toFloat() / ratio).toInt()
            }

            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

}