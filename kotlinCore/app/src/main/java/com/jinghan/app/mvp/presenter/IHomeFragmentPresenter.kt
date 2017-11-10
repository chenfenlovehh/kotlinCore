package com.jinghan.app.mvp.presenter

import android.widget.ImageView
import com.jinghan.app.mvp.view.impl.view.IHomeFragmentView
import com.jinghan.core.mvp.preseneter.BaseLifecyclePresenter
import com.trello.rxlifecycle2.android.FragmentEvent

/**
 * @author liuzeren
 * @time 2017/11/10    下午3:18
 * @mail lzr319@163.com
 */
abstract class IHomeFragmentPresenter : BaseLifecyclePresenter<IHomeFragmentView,FragmentEvent>(){

    /**底部栏接口*/
    abstract fun homeBottomBars()

    /**动态加载底部栏图标*/
    abstract fun initImage(imageView: ImageView, normalUrl: String?, selectedUrl: String?)

}