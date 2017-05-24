package me.zsj.pretty_girl_kotlin.widget

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout

/**
 * @author zsj
 */
class PullBackLayout : FrameLayout {

    private var dragHelper: ViewDragHelper? = null
    private var pullCallback: PullCallback? = null

    private var mMinimumFlingVelocity: Int = 0


    fun setPullCallback(pullCallback: PullCallback) {
        this.pullCallback = pullCallback
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, set: AttributeSet?) : this(context, set, 0)

    constructor(context: Context, set: AttributeSet?, defAttr: Int) :
            super(context, set, defAttr) {

        dragHelper = ViewDragHelper.create(this, 1.toFloat() / 8.toFloat(), dragCallback)

        mMinimumFlingVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity
    }

    private var dragCallback = object : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View?, pointerId: Int): Boolean {
            return true
        }

        override fun clampViewPositionVertical(child: View?,
                                               top: Int, dy: Int): Int {
            return Math.max(0, top)
        }

        override fun getViewHorizontalDragRange(child: View?): Int {
            return 0
        }

        override fun clampViewPositionHorizontal(child: View?,
                                                 left: Int, dx: Int): Int {
            return 0
        }

        override fun getViewVerticalDragRange(child: View?): Int {
            return height
        }

        override fun onViewCaptured(capturedChild: View?, activePointerId: Int) {
            super.onViewCaptured(capturedChild, activePointerId)

            pullCallback!!.onPullStart()
        }

        override fun onViewReleased(releasedChild: View?, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            val slop = if (yvel > mMinimumFlingVelocity) height / 6
            else height / 3
            if (releasedChild!!.top > slop) {
                pullCallback!!.onPullCompleted()
            } else {
                dragHelper!!.settleCapturedViewAt(0, 0)
                invalidate()
            }

        }

        override fun onViewPositionChanged(changedView: View?,
                                           left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            val progress = Math.min(1f, top.toFloat() / height.toFloat() * 5f)
            pullCallback!!.onPull(progress)
        }

    }

    override fun computeScroll() {
        super.computeScroll()
        if (dragHelper!!.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        try {
            return dragHelper!!.shouldInterceptTouchEvent(ev)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        dragHelper?.processTouchEvent(event)
        return true
    }

    interface PullCallback {

        fun onPullStart()

        fun onPull(progress: Float)

        fun onPullCompleted()

    }

}