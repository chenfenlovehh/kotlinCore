package com.jinghan.app.mvp.view.activity

import android.Manifest
import android.support.v4.app.Fragment
import com.jinghan.app.mvp.presenter.ISplashActivityPresenter
import com.jinghan.app.mvp.view.fragment.GuideFragment
import com.jinghan.app.mvp.view.fragment.SplashFragment
import com.jinghan.core.R
import com.jinghan.core.databinding.AtySplashBinding
import com.jinghan.core.dependencies.aspectj.annotation.Permission
import com.jinghan.core.helper.AndroidUtils
import com.jinghan.core.mvp.view.activity.BaseActivity
import javax.inject.Inject

import dagger.Lazy

/**
 * @author liuzeren
 * @time 2017/11/6    下午2:07
 * @mail lzr319@163.com
 */
class SplashActivity(override val layoutId: Int = R.layout.aty_splash) : BaseActivity<AtySplashBinding>(){

    @Inject lateinit var splashFragment : Lazy<SplashFragment>
    @Inject lateinit var guideFragment: Lazy<GuideFragment>

    @Inject lateinit var presenter:ISplashActivityPresenter

    @Permission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    override fun initViewsAndListener() {
    }

    override fun initData() {
        applyFragment(presenter.isFirst)
    }

    override fun initPresenter() {
        presenter.takeView(this)
    }

    override fun initToolbar() {
    }

    private fun applyFragment(isFirstRun:Boolean){
        var fragment : Fragment

        if(isFirstRun)
            fragment = guideFragment.get()
        else
            fragment = splashFragment.get()

        AndroidUtils.addFragmentToActivity(supportFragmentManager,fragment,R.id.flContainer)
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }
}