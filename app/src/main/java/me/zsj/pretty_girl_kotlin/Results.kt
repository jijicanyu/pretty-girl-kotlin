package me.zsj.pretty_girl_kotlin

import me.zsj.pretty_girl_kotlin.model.GirlData
import me.zsj.pretty_girl_kotlin.model.Image
import retrofit2.adapter.rxjava.Result
import rx.functions.Func1

/**
 * @author zsj
 */
class Results<T> private constructor() {

    companion object {
        val DATA_FUNC = Func1<Result<GirlData>, Boolean> {
            result -> !result!!.isError && result.response().isSuccessful
        }

        fun isSuccess() : Func1<Result<GirlData>, Boolean> {
            return DATA_FUNC
        }

        val IMAGE_FUNC = Func1<List<Image>, Boolean> {
            images -> images.isNotEmpty()
        }

        fun isNull() : Func1<List<Image>, Boolean> {
            return IMAGE_FUNC
        }
    }

}