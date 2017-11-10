package com.jinghan.app.mvp.view.fragment

import android.content.Intent
import com.jinghan.app.mvp.model.bean.SplashInfo
import com.jinghan.app.mvp.presenter.ISplashFragmentPresenter
import com.jinghan.app.mvp.view.activity.HomeActivity
import com.jinghan.app.mvp.view.impl.view.ISplashFragmentView
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
constructor() : BaseFragment<FgSplashBinding>(),ISplashFragmentView {

    @Inject
    protected lateinit var presenter : ISplashFragmentPresenter

    override val layoutId: Int
        get() = R.layout.fg_splash

    override fun initData() {
        presenter.reqSplashInfo()

    }

    override fun initPresenter() {
        presenter.takeView(this)
        presenter.lifecycle(lifecycleSubject)
    }

    override fun updateSplash(splashInfo: SplashInfo?) {
        presenter.interval(binding.viewContainer,binding.viewProgress,binding.pbCircle,binding.tvProgress,splashInfo)
    }

    override fun toMain() {
        mActivity?.finish()
        var intent = Intent(context,HomeActivity::class.java)
        startActivity(intent)
    }
}