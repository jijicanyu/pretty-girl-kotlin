package me.zsj.pretty_girl_kotlin

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author zsj
 */
class GirlRetrofit {

    private val GANK_URL: String = "http://gank.io/api/"
    private var girlApi: GirlApi

    init {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(20, TimeUnit.SECONDS)
        builder.readTimeout(15, TimeUnit.SECONDS)
        val retrofit = Retrofit.Builder()
                .baseUrl(GANK_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(builder.build())
                .build()
        girlApi = retrofit.create(GirlApi::class.java)
    }

    fun girlApi(): GirlApi {
        return girlApi
    }

}