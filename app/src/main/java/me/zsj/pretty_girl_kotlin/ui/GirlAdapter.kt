package me.zsj.pretty_girl_kotlin.ui

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jakewharton.rxbinding.view.RxView
import me.zsj.pretty_girl_kotlin.R
import me.zsj.pretty_girl_kotlin.databinding.GirlItemBinding
import me.zsj.pretty_girl_kotlin.model.Image
import rx.functions.Action1
import java.util.concurrent.TimeUnit

/**
 * @author zsj
 */
class GirlAdapter : RecyclerView.Adapter<GirlAdapter.Holder>, Action1<List<Image>> {

    private var images: List<Image>? = null
    private var onTouchListener: OnTouchListener? = null


    constructor(images: List<Image>) {
        this.images = images
    }

    fun setOnTouchListener(onTouchListener: OnTouchListener) {
        this.onTouchListener = onTouchListener
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.girl_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        val image = images!![position]
        holder!!.image = image
        holder.binding!!.setImage(image)
        holder.binding!!.executePendingBindings()

        Glide.with(holder.binding!!.image.context)
                .load(image.url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.binding!!.image)
    }

    override fun getItemCount(): Int {
        return images!!.size
    }

    override fun getItemViewType(position: Int): Int {
        val image = images!![position]
        return Math.round(image.width!!.toFloat() / image.height!!.toFloat())
    }

    override fun call(t: List<Image>?) {
        notifyDataSetChanged()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var binding: GirlItemBinding? = null
        var image: Image? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            RxView.clicks(binding!!.girlLayout)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe{ aVoid ->
                        onTouchListener!!.onImageClick(binding!!.image, image!!)
                    }
        }

    }

    interface OnTouchListener {
        fun onImageClick(v: View, image: Image)
    }

}