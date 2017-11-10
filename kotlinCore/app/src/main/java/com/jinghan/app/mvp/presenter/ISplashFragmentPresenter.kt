package com.jinghan.app.mvp.presenter

import android.view.ViewGroup
import android.widget.TextView
import com.jinghan.app.mvp.model.bean.SplashInfo
import com.jinghan.app.mvp.view.impl.view.ISplashFragmentView
import com.jinghan.core.mvp.preseneter.BaseLifecyclePresenter
import com.jinghan.core.mvp.widget.CircleProgressBar
import com.trello.rxlifecycle2.android.FragmentEvent

/**
 * @author liuzeren
 * @time 2017/11/9    上午11:03
 * @mail lzr319@163.com
 */
abstract class ISplashFragmentPresenter : BaseLifecyclePresenter<ISplashFragmentView, FragmentEvent>() {

    abstract fun reqSplashInfo()

    abstract fun interval(viewGroup: ViewGroup, viewProgress: ViewGroup, pbCircle: CircleProgressBar, tvProgress: TextView, splashInfo: SplashInfo?)

}