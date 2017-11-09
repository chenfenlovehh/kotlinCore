package com.jinghan.core.mvp.preseneter

import io.reactivex.subjects.BehaviorSubject

/**
 * @author liuzeren
 * @time 2017/11/9    上午11:27
 * @mail lzr319@163.com
 */
interface BaseLifecyclePresenter<T,V> : BasePresenter<T> {

    fun lifecycle(lifecycleSubject:BehaviorSubject<V>)

}