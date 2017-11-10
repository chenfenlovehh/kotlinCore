package com.jinghan.app.mvp.presenter.impl

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.jinghan.app.mvp.model.bean.SplashInfo
import com.jinghan.app.mvp.model.request.BaseRequest
import com.jinghan.app.mvp.model.request.BaseUserRequestData
import com.jinghan.app.mvp.model.response.SplashResponse
import com.jinghan.app.mvp.presenter.ISplashFragmentPresenter
import com.jinghan.app.mvp.view.impl.api.SplashService
import com.jinghan.app.mvp.view.impl.view.ISplashFragmentView
import com.jinghan.core.dependencies.http.OkHttp
import com.jinghan.core.mvp.widget.CircleProgressBar
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DefaultObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.Glide
import com.jinghan.core.R
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


/**
 * @author liuzeren
 * @time 2017/11/9    上午11:04
 * @mail lzr319@163.com
 */
class SplashFragmentPresenter @Inject constructor(val context:Context) : ISplashFragmentPresenter() {

    @Inject
    protected lateinit var mOkHttp:OkHttp

    private var mISplashFragmentView : ISplashFragmentView? = null

    override fun reqSplashInfo() {
        val request = BaseRequest<BaseUserRequestData>("loadingPageProvider","queryLoadingPageInfo",data = BaseUserRequestData())

        mOkHttp.retrofit.builder<SplashService>(SplashService::class.java).queryLoadingPageInfo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleAndroid.bindFragment(lifecycleSubject))
                .map({t: SplashResponse -> t.resultData })
                .subscribe(object : DefaultObserver<SplashInfo>() {

            private var splashInfo: SplashInfo? = null

            override fun onNext(@io.reactivex.annotations.NonNull splashInfo: SplashInfo) {
                this.splashInfo = splashInfo
            }

            override fun onError(@io.reactivex.annotations.NonNull e: Throwable) {
                mISplashFragmentView?.updateSplash(splashInfo)
            }

            override fun onComplete() {
                mISplashFragmentView?.updateSplash(splashInfo)
            }
        })
    }

    override fun interval(viewGroup: ViewGroup, viewProgress: ViewGroup, pbCircle: CircleProgressBar, tvProgress: TextView, splashInfo: SplashInfo?) {
        if (null == splashInfo) {
            mISplashFragmentView?.toMain()
            return
        }

        pbCircle.max = splashInfo.countDown * 1000
        pbCircle.progress = splashInfo.countDown
        tvProgress.setText(context.getString(R.string.skip_progress, splashInfo.countDown))

        if (!TextUtils.isEmpty(splashInfo.initialImageUrl)) {
            Glide.with(context).load(splashInfo.initialImageUrl).fitCenter().into(object : ViewTarget<ViewGroup, GlideDrawable>(viewGroup) {
                override fun onResourceReady(resource: GlideDrawable, glideAnimation: GlideAnimation<in GlideDrawable>) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        viewGroup.background = resource
                    } else {
                        viewGroup.setBackgroundDrawable(resource)
                    }

                    val mDefaultObserver = object : DefaultObserver<Long>() {

                        init {
                            viewProgress.setOnClickListener {
                                cancel()
                                mISplashFragmentView?.toMain()
                            }
                        }

                        override fun onNext(@io.reactivex.annotations.NonNull o: Long) {
                            tvProgress.setText(context.getString(R.string.skip_progress, (splashInfo.countDown * 1000 - o) / 1000 + 1))
                            pbCircle.progress = splashInfo.countDown * 1000 - o.toInt()
                        }

                        override fun onError(@io.reactivex.annotations.NonNull e: Throwable) {
                            //理论上是不会发生这一错误的
                            mISplashFragmentView?.toMain()
                        }

                        override fun onComplete() {
                            mISplashFragmentView?.toMain()
                        }
                    }
                    viewProgress.visibility = View.VISIBLE

                    //图片加载成功，则启动计时
                    Observable.interval(0, 1, TimeUnit.MILLISECONDS).compose(RxLifecycleAndroid.bindFragment(lifecycleSubject))
                            .takeWhile({ it <= splashInfo.countDown * 1000 }).observeOn(AndroidSchedulers.mainThread()).subscribe(mDefaultObserver)
                }

                override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                    super.onLoadFailed(e, errorDrawable)

                    mISplashFragmentView?.toMain()
                }
            })
        } else {
            mISplashFragmentView?.toMain()
        }
    }

    override fun takeView(view: ISplashFragmentView) {
        this.mISplashFragmentView = view
    }

    override fun dropView() {
        mISplashFragmentView = null
    }
}