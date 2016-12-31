package me.zsj.pretty_girl_kotlin.model;

import retrofit2.adapter.rxjava.Result;
import rx.functions.Func1;

/**
 * @author zsj
 */

public class Haha {

    public static Func1<Result<?>, Boolean> RESULT_FUNC =
            new Func1<Result<?>, Boolean>() {
                @Override
                public Boolean call(Result<?> result) {
                    return !result.isError() && result.response().isSuccessful();
                }
            };

    public static Func1<Result<?>, Boolean> isSuccess() {
        return RESULT_FUNC;
    }
}
