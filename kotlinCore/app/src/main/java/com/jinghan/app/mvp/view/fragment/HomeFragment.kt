package com.jinghan.app.mvp.view.fragment

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TabHost
import com.jinghan.app.mvp.model.bean.CatalogInfo
import com.jinghan.app.mvp.presenter.IHomeFragmentPresenter
import com.jinghan.app.mvp.view.impl.view.IHomeFragmentView
import com.jinghan.core.databinding.FgHomeBinding
import com.jinghan.core.mvp.view.fragment.BaseFragment
import javax.inject.Inject
import com.jinghan.core.R
import com.jinghan.core.databinding.FgHomeBottomBarBinding
import com.jinghan.core.dependencies.dragger2.scope.ActivityScoped
import io.reactivex.Flowable
import java.util.ArrayList

/**
 * 首页
 * @author liuzeren
 * @time 2017/11/10    上午11:33
 * @mail lzr319@163.com
 */
@ActivityScoped
class HomeFragment @SuppressLint("ValidFragment")
@Inject constructor() : BaseFragment<FgHomeBinding>(), IHomeFragmentView {

    protected @Inject lateinit var presenter : IHomeFragmentPresenter

    override val layoutId: Int
        get() = R.layout.fg_home

    override fun initViews() {
        super.initViews()

        binding.tabHost.setup(context, fragmentManager, R.id.realtabcontent)
        binding.tabHost.setOnTabChangedListener { tabId -> toast(tabId) }
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            binding.tabHost.tabWidget.showDividers = 0
        }
    }

    override fun initData() {
        presenter.homeBottomBars()
    }

    override fun initPresenter() {
        presenter.takeView(this)
        presenter.lifecycle(lifecycleSubject)
    }

    override fun updateBottomBars(catalogInfos: ArrayList<CatalogInfo>?) {

        data class TabInfo(val tabSpec: TabHost.TabSpec,val bundle: Bundle)

        Flowable.fromIterable(catalogInfos).map { info ->
            val tabSpec = binding.tabHost.newTabSpec(info.jumpType)

            val bottomBarBinding = DataBindingUtil.inflate<FgHomeBottomBarBinding>(LayoutInflater.from(context), R.layout.fg_home_bottom_bar, null, false)
            bottomBarBinding.tvTitle.text = info.catalogName
            presenter.initImage(bottomBarBinding.ivImage, info.logo, info.selectedLogo)
            tabSpec.setIndicator(bottomBarBinding.root)

            val bundle = Bundle()
            bundle.putString("service", info.service)
            bundle.putString("method", info.method)
            bundle.putLong("id", info.catalogId)

            TabInfo(tabSpec,bundle)
        }.subscribe({ tabData -> binding.tabHost.addTab(tabData.tabSpec, RecommendFragment::class.java, tabData.bundle) })
    }
}