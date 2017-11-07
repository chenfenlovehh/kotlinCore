package com.jinghan.app.mvp.view.fragment

import com.jinghan.core.R
import com.jinghan.core.databinding.FgSplashBinding
import com.jinghan.core.dependencies.dragger2.scope.ActivityScoped
import com.jinghan.core.mvp.view.fragment.BaseFragment
import javax.inject.Inject

/**
 * @author liuzeren
 * @time 2017/11/7    下午6:02
 * @mail lzr319@163.com
 */
@ActivityScoped
class SplashFragment @Inject
constructor() : BaseFragment<FgSplashBinding>() {
    override val layoutId: Int
        get() = R.layout.fg_splash

    override fun initData() {}

    override fun initPresenter() {}
}