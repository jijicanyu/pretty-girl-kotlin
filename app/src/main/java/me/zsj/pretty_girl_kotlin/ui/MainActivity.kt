package me.zsj.pretty_girl_kotlin.ui

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.Toast
import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewScrollEvent
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding.view.RxView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle.android.ActivityEvent
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import me.zsj.pretty_girl_kotlin.GirlApi
import me.zsj.pretty_girl_kotlin.GirlApiComponent
import me.zsj.pretty_girl_kotlin.R
import me.zsj.pretty_girl_kotlin.Results
import me.zsj.pretty_girl_kotlin.databinding.MainActivityBinding
import me.zsj.pretty_girl_kotlin.model.GirlData
import me.zsj.pretty_girl_kotlin.model.Image
import me.zsj.pretty_girl_kotlin.utils.ConfigUtils
import me.zsj.pretty_girl_kotlin.utils.NetUtils
import retrofit2.adapter.rxjava.Result
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class MainActivity : RxAppCompatActivity() {

    private var binding: MainActivityBinding? = null
    private var adapter: GirlAdapter? = null

    @Inject lateinit var girlApi: GirlApi
    private var layoutManager: StaggeredGridLayoutManager? = null
    private var page: Int = 1
    private var refreshing: Boolean = false
    private var images = ArrayList<Image>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity)
        setSupportActionBar(binding!!.toolbar)

        GirlApiComponent.Initializer.init().inject(this)

        flyToTop()
        swipeRefresh()
        setupRecyclerView()
        onImageClick()
    }

    fun flyToTop() {
        RxView.clicks(binding!!.toolbar)
                .compose(bindToLifecycle<Void>())
                .subscribe {
                    layoutManager!!.scrollToPositionWithOffset(0, 0)
                }
    }

    fun swipeRefresh() {
        RxSwipeRefreshLayout.refreshes(binding!!.refreshLayout)
                .compose(bindToLifecycle<Void>())
                .subscribe {
                    page = 1
                    refreshing = true
                    fetchGirlData()
                }
    }

    fun setupRecyclerView() {
        adapter = GirlAdapter(images)
        var spanCount = if (ConfigUtils.isOrientationPortrait(this)) 2
        else 3

        layoutManager = StaggeredGridLayoutManager(spanCount,
                StaggeredGridLayoutManager.VERTICAL)
        binding!!.recyclerView.layoutManager = layoutManager
        binding!!.recyclerView.adapter = adapter

        RxRecyclerView.scrollEvents(binding!!.recyclerView)
                .compose(bindUntilEvent<RecyclerViewScrollEvent>(ActivityEvent.DESTROY))
                .map {
                    val isBottom = if (ConfigUtils.isOrientationPortrait(this)) {
                        layoutManager!!.findLastCompletelyVisibleItemPositions(
                                IntArray(2))[1] >= this.images.size - 4
                    } else {
                         layoutManager!!.findLastCompletelyVisibleItemPositions(
                                IntArray(3))[2] >= this.images.size - 4
                    }
                    return@map isBottom
                }
                .filter { isBottom -> !binding!!.refreshLayout.isRefreshing && isBottom }
                .subscribe {
                    //这么做的目的是一旦下拉刷新，RxRecyclerView scrollEvents 也会被触发，page就会加一
                    //所以要将page设为0，这样下拉刷新才能获取第一页的数据
                    if (refreshing) {
                        page = 0
                        refreshing = false
                    }
                    page += 1
                    binding!!.refreshLayout.isRefreshing = true
                    fetchGirlData()
                }
    }

    fun onImageClick() {
        adapter!!.setOnTouchListener(object : GirlAdapter.OnTouchListener {
            override fun onImageClick(v: View, image: Image) {
                Picasso.with(this@MainActivity).load(image.url)
                        .fetch(object : Callback {
                            override fun onSuccess() {
                                val intent = Intent(this@MainActivity, PictureActivity::class.java)
                                intent.putExtra("url", image.url)
                                val compat =
                                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                                this@MainActivity, v, "girl"
                                        )
                                ActivityCompat.startActivity(this@MainActivity, intent,
                                        compat.toBundle())
                            }

                            override fun onError() {
                            }
                        })
            }
        })
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (!NetUtils.checkNet(this)) {
            Toast.makeText(this, "不开网络没妹纸看哟!", Toast.LENGTH_LONG).show()
        }
        fetchGirlData()
    }

    fun fetchGirlData() {
        val results = girlApi.fetchPrettyGirl(page)
                .compose(bindToLifecycle<Result<GirlData>>())
                .filter(Results.isSuccess())
                .filter { it.response().body() != null } //it represent girlData
                .map { it.response().body() }
                .flatMap(imageFetcher)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()

        results.filter(Results.isNull())
                .doOnCompleted { binding!!.refreshLayout.isRefreshing = false }
                .subscribe(adapter, dataError)
    }

    private var imageFetcher = Func1<GirlData, Observable<List<Image>>> {
        val girls = it.results!! //it represent girlData
        for (girl in girls) {
            val bitmap = Picasso.with(this).load(girl.url).get()
            images.add(Image(bitmap.width, bitmap.height, girl.url))
        }
        return@Func1 Observable.just(images)
    }

    private var dataError = Action1<Throwable> { throwable ->
        throwable.printStackTrace()
        binding!!.refreshLayout.isRefreshing = false
        Toast.makeText(this, throwable.message, Toast.LENGTH_SHORT).show()
    }

}
