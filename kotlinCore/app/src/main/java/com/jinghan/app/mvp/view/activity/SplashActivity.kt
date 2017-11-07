package com.jinghan.app.mvp.view.activity

import com.jinghan.app.mvp.view.fragment.GuideFragment
import com.jinghan.app.mvp.view.fragment.SplashFragment
import com.jinghan.core.R
import com.jinghan.core.databinding.AtySplashBinding
import com.jinghan.core.dependencies.aspectj.annotation.Permission
import com.jinghan.core.mvp.view.activity.BaseActivity
import javax.inject.Inject

/**
 * @author liuzeren
 * @time 2017/11/6    下午2:07
 * @mail lzr319@163.com
 */
class SplashActivity(override val layoutId: Int = R.layout.aty_splash) : BaseActivity<AtySplashBinding>(){

    @Inject lateinit var splashFragment : Lazy<SplashFragment>
    @Inject lateinit var guideFragment: Lazy<GuideFragment>

    @Permission
    override fun initViewsAndListener() {
    }

    override fun initData() {
    }

    override fun initPresenter() {
    }

    override fun initToolbar() {
    }


}