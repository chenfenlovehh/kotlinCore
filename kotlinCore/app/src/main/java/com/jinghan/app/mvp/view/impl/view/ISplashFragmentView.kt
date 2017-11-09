package com.jinghan.app.mvp.view.impl.view

import com.jinghan.app.mvp.model.bean.SplashInfo
import com.jinghan.core.mvp.view.impl.view.BaseView

/**
 * @author liuzeren
 * @time 2017/11/9    上午11:03
 * @mail lzr319@163.com
 */
interface ISplashFragmentView : BaseView{

    /**
     * 更新欢迎页信息
     * */
    fun updateSplash(splashInfo: SplashInfo?)

    /**
     * 跳转到主页
     * */
    fun toMain()
}