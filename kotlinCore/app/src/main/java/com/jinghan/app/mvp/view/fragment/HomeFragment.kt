package com.jinghan.app.mvp.view.fragment

import android.annotation.SuppressLint
import com.jinghan.core.databinding.FgHomeBinding
import com.jinghan.core.mvp.view.fragment.BaseFragment
import javax.inject.Inject
import com.jinghan.core.R
import com.jinghan.core.dependencies.dragger2.scope.ActivityScoped

/**
 * 首页
 * @author liuzeren
 * @time 2017/11/10    上午11:33
 * @mail lzr319@163.com
 */
@ActivityScoped
class HomeFragment @SuppressLint("ValidFragment")
@Inject constructor() : BaseFragment<FgHomeBinding>() {
    override val layoutId: Int
        get() = R.layout.fg_home

    override fun initData() {

    }

    override fun initPresenter() {
    }
}