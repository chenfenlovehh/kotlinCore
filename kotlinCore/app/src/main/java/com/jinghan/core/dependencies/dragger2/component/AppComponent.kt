package com.jinghan.core.dependencies.dragger2.component

import android.app.Application
import com.jinghan.app.AppContext
import com.jinghan.core.dependencies.dragger2.module.ActivityBindingModule
import com.jinghan.core.dependencies.dragger2.module.ApplicationModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * @author liuzeren
 * @time 2017/11/2    下午3:18
 * @mail lzr319@163.com
 */
@Singleton
@Component(modules = arrayOf(ApplicationModule::class
        ,AndroidSupportInjectionModule::class
        ,ActivityBindingModule::class))
interface AppComponent : AndroidInjector<DaggerApplication>{

    fun inject(appContext:AppContext)

    override fun inject(daggerApplication: DaggerApplication)

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application:Application) : AppComponent.Builder

        fun build() : AppComponent
    }
}