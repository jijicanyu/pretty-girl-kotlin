package me.zsj.pretty_girl_kotlin.module

import dagger.Module
import dagger.Provides
import me.zsj.pretty_girl_kotlin.GirlApi
import me.zsj.pretty_girl_kotlin.GirlRetrofit
import javax.inject.Singleton

/**
 * @author zsj
 */
@Module
class GirlApiModule {

    @Provides @Singleton
    fun provideGirlApi() : GirlApi {
        return GirlRetrofit().girlApi()
    }

}