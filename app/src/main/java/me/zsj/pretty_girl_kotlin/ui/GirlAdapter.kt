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
class GirlAdapter(images: List<Image>, val onTouchListener: (View, Image) -> Unit) :
        RecyclerView.Adapter<GirlAdapter.Holder>(), Action1<List<Image>> {

    private var images: List<Image>? = images

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.girl_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        val image = images!![position]

        holder?.binding?.setImage(image)
        holder?.binding?.executePendingBindings()

        with(image) {
            RxView.clicks(holder?.binding!!.girlLayout)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        onTouchListener(holder.binding!!.image, this)
                    }

            Glide.with(holder.binding?.image?.context)
                    .load(this.url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.binding?.image)
        }
    }

    override fun getItemCount(): Int {
        return images!!.size
    }

    override fun getItemViewType(position: Int): Int {
        val image = images!![position]
        return Math.round(image.width.toFloat() / image.height.toFloat() * 10f)
    }

    override fun getItemId(position: Int): Long {
        return images!![position].hashCode().toLong()
    }

    override fun call(t: List<Image>?) {
        notifyDataSetChanged()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var binding: GirlItemBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
        }

    }

}