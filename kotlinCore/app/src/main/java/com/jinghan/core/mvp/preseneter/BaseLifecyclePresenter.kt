package com.jinghan.core.mvp.preseneter

import com.jinghan.core.dependencies.http.OkHttp
import com.jinghan.core.mvp.view.impl.view.BaseView
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

/**
 * @author liuzeren
 * @time 2017/11/9    上午11:27
 * @mail lzr319@163.com
 */
abstract class BaseLifecyclePresenter<T : BaseView,V> : BasePresenter<T> {

    protected var mView : T? = null

    @Inject
    protected lateinit var mOkHttp: OkHttp

    override fun takeView(view: T) {
        mView = view
    }

    override fun dropView() {
        mView = null
    }

    protected lateinit var lifecycleSubject: BehaviorSubject<V>

    fun lifecycle(lifecycleSubject:BehaviorSubject<V>){
        this.lifecycleSubject = lifecycleSubject
    }

}