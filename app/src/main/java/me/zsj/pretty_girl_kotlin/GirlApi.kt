package me.zsj.pretty_girl_kotlin

import me.zsj.pretty_girl_kotlin.model.GirlData
import retrofit2.adapter.rxjava.Result
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

/**
 * @author zsj
 */
interface GirlApi {

    @GET("data/福利/10/{page}")
    fun fetchPrettyGirl(@Path("page") page: Int) : Observable<Result<GirlData>>

}