package me.zsj.pretty_girl_kotlin

import dagger.Component
import me.zsj.pretty_girl_kotlin.module.GirlApiModule
import javax.inject.Singleton

/**
 * @author zsj
 */

@Singleton
@Component(modules = arrayOf(GirlApiModule::class))
interface GirlApiComponent : GirlGraph {

    class Initializer private constructor() {

        companion object {
            @JvmStatic fun init() : GirlApiComponent {
                return DaggerGirlApiComponent.builder()
                        .girlApiModule(GirlApiModule())
                        .build()
            }
        }
    }

}