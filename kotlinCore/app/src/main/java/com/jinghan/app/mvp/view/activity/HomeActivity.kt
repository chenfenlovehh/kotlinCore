package com.jinghan.app.mvp.view.activity

import com.airbnb.deeplinkdispatch.DeepLink
import com.jinghan.app.global.Constant.APP_SCHEME
import com.jinghan.app.mvp.presenter.IHomeActivityPresenter
import com.jinghan.app.mvp.view.fragment.HomeFragment
import com.jinghan.core.databinding.AtyHomeBinding
import com.jinghan.core.mvp.view.activity.BaseActivity
import com.jinghan.core.R
import com.jinghan.core.helper.AndroidUtils
import dagger.Lazy
import javax.inject.Inject

/**
 * @author liuzeren
 * @time 2017/11/9    上午10:52
 * @mail lzr319@163.com
 */
@DeepLink("${APP_SCHEME}home")
class HomeActivity(override val layoutId: Int = R.layout.aty_home) : BaseActivity<AtyHomeBinding>(){

    @Inject
    lateinit var homeFragment : Lazy<HomeFragment>

    @Inject
    lateinit var presenter : IHomeActivityPresenter

    override fun initViewsAndListener() {
        var rxFragment = supportFragmentManager.findFragmentByTag(HomeFragment::class.java.getSimpleName())
        AndroidUtils.addFragmentToActivity(
                supportFragmentManager, rxFragment?:homeFragment.get(), R.id.fl_container)
    }

    override fun initData() {
    }

    override fun initPresenter() {
        presenter.takeView(this)
        presenter.lifecycle(lifecycleSubject)
    }

    override fun initToolbar() {
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }
}