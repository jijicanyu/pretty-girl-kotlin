package me.zsj.pretty_girl_kotlin.ui

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.view.KeyEvent
import android.view.View
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import me.zsj.pretty_girl_kotlin.R
import me.zsj.pretty_girl_kotlin.databinding.PictureActivityBinding
import me.zsj.pretty_girl_kotlin.widget.PullBackLayout
import uk.co.senab.photoview.PhotoViewAttacher

/**
 * @author zsj
 */
class PictureActivity : RxAppCompatActivity(), PullBackLayout.PullCallback {

    private var systemUiIsShow: Boolean = true
    private var background: ColorDrawable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<PictureActivityBinding>(
                this, R.layout.picture_activity)

        val mGirlUrl = intent.extras.getString("url")

        ViewCompat.setTransitionName(binding!!.ivPhoto, "girl")

        Picasso.with(this).load(mGirlUrl).into(binding.ivPhoto)

        background = ColorDrawable(Color.BLACK)

        binding.pullBackLayout.rootView.background = background

        val viewAttacher = PhotoViewAttacher(binding.ivPhoto)

        binding.pullBackLayout.setPullCallback(this)

        viewAttacher.setOnViewTapListener { view, x, y ->
            if (systemUiIsShow) {
                hideSystemUi()
                systemUiIsShow = false
            } else {
                showSystemUi()
                systemUiIsShow = true
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        showSystemUi()
        return super.onKeyDown(keyCode, event)
    }

    private val FLAG_HIDE_SYSTEM_UI: Int = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_IMMERSIVE

    private val FLAG_SHOW_SYSTEM_UI: Int = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

    private fun hideSystemUi() {
        window.decorView.systemUiVisibility = FLAG_HIDE_SYSTEM_UI
    }

    private fun showSystemUi() {
        window.decorView.systemUiVisibility = FLAG_SHOW_SYSTEM_UI
    }

    override fun onPullStart() {
        showSystemUi()
    }

    override fun onPull(progress: Float) {
        showSystemUi()
        background!!.alpha = (0xff * (1.toFloat() - progress)).toInt()
    }

    override fun onPullCompleted() {
        showSystemUi()
        finishAfterTransition()
    }

}