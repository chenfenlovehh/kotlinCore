package com.jinghan.core.dependencies.dragger2.module

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

/**
 * @author liuzeren
 * @time 2017/11/2    下午3:15
 * @mail lzr319@163.com
 */
@Module
abstract class ApplicationModule{

    @Binds
    abstract fun bindContext(app:Application):Context
}