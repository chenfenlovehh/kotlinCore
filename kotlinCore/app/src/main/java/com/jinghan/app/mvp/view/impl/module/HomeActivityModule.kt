package com.jinghan.app.mvp.view.impl.module

import com.jinghan.app.mvp.presenter.IHomeActivityPresenter
import com.jinghan.app.mvp.presenter.IHomeFragmentPresenter
import com.jinghan.app.mvp.presenter.impl.HomeActivityPresenter
import com.jinghan.app.mvp.presenter.impl.HomeFragmentPresenter
import com.jinghan.app.mvp.view.fragment.HomeFragment
import com.jinghan.app.mvp.view.fragment.RecommendFragment
import com.jinghan.core.dependencies.dragger2.scope.ActivityScoped
import com.jinghan.core.dependencies.dragger2.scope.FragmentScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author liuzeren
 * @time 2017/11/10    下午2:03
 * @mail lzr319@163.com
 */
@Module
abstract class HomeActivityModule{

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun homeFragment() : HomeFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun recommendFragment() : RecommendFragment

    @ActivityScoped
    @Binds
    abstract fun homeActivityPresenter(homeActivityPresenter: HomeActivityPresenter) : IHomeActivityPresenter

    @ActivityScoped
    @Binds
    abstract fun homeFragmentPresenter(homeFragmentPresenter: HomeFragmentPresenter) : IHomeFragmentPresenter

}