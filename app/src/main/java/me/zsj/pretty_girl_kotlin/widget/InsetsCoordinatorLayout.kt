package me.zsj.pretty_girl_kotlin.widget

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.View
import android.view.WindowInsets
import me.zsj.pretty_girl_kotlin.R

/**
 * @author zsj
 */
class InsetsCoordinatorLayout : CoordinatorLayout, View.OnApplyWindowInsetsListener {

    private var recyclerView: RecyclerView? = null
    private var toolbar: Toolbar? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, set: AttributeSet?) : this(context, set, 0)

    constructor(context: Context, set: AttributeSet?, defStyleAttr: Int) :
            super(context, set, defStyleAttr) {
        setOnApplyWindowInsetsListener(this)
    }

    override fun onApplyWindowInsets(v: View, insets: WindowInsets): WindowInsets {
        val l = insets.systemWindowInsetLeft
        val t = insets.systemWindowInsetTop
        val r = insets.systemWindowInsetRight
        toolbar?.setPadding(l, t, 0, 0)

        val ltr = recyclerView!!.layoutDirection == View.LAYOUT_DIRECTION_LTR

        var paddingRight = 0
        if (ltr) paddingRight += r
        else paddingRight = paddingEnd

        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)

        setOnApplyWindowInsetsListener(null)
        return insets.consumeSystemWindowInsets()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        toolbar = findViewById(R.id.toolbar) as Toolbar
        recyclerView = findViewById(R.id.recyclerView) as RecyclerView
    }

}