package com.jinghan.app.mvp.view.fragment

import android.content.Intent
import android.view.View
import com.jinghan.app.mvp.view.activity.HomeActivity
import com.jinghan.app.mvp.view.adapter.GuideAdapter
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
    constructor() : BaseFragment<FgGuideBinding>(),View.OnClickListener {

    private lateinit var adapter : GuideAdapter

    override val layoutId: Int
        get() = R.layout.fg_guide

    override fun initData() {
        adapter = GuideAdapter(context, arrayOf(R.mipmap.wel1,R.mipmap.wel2,R.mipmap.wel3,R.mipmap.wel4))
        adapter.itemClick = this
        binding.viewPager.adapter = adapter
    }

    override fun onClick(v: View?) {
        mActivity?.finish()
        val intent = Intent(context, HomeActivity::class.java)
        startActivity(intent)
    }

    override fun initPresenter() {}
}