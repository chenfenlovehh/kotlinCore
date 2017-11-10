package com.jinghan.app.mvp.view.fragment

import com.jinghan.core.databinding.FgRecommendBinding
import com.jinghan.core.mvp.view.fragment.BaseFragment
import com.jinghan.core.R
import com.jinghan.core.dependencies.dragger2.scope.ActivityScoped
import javax.inject.Inject

/**
 * @author liuzeren
 * @time 2017/11/10    下午5:29
 * @mail lzr319@163.com
 */
@ActivityScoped
class RecommendFragment @Inject constructor() : BaseFragment<FgRecommendBinding>(){
    override val layoutId: Int = R.layout.fg_recommend

    override fun initData() {
    }

    override fun initPresenter() {
    }
}