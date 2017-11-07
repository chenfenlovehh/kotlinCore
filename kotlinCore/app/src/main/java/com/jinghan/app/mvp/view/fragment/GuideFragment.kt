package com.jinghan.app.mvp.view.fragment

import com.jinghan.core.R
import com.jinghan.core.databinding.FgGuideBinding
import com.jinghan.core.dependencies.dragger2.scope.ActivityScoped
import com.jinghan.core.mvp.view.fragment.BaseFragment
import javax.inject.Inject

/**
 * 欢迎引导页
 * @author liuzeren
 * @time 2017/11/7    下午6:03
 * @mail lzr319@163.com
 */
@ActivityScoped
class GuideFragment @Inject
    constructor() : BaseFragment<FgGuideBinding>() {
    override val layoutId: Int
        get() = R.layout.fg_guide

    override fun initData() {}

    override fun initPresenter() {}
}