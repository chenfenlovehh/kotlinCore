package com.jinghan.app.mvp.presenter.impl

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.jinghan.app.mvp.model.bean.CatalogInfo
import com.jinghan.app.mvp.model.request.BaseRequest
import com.jinghan.app.mvp.model.request.BaseUserRequestData
import com.jinghan.app.mvp.model.response.CatalogListResponse
import com.jinghan.app.mvp.presenter.IHomeFragmentPresenter
import com.jinghan.app.mvp.view.impl.api.HomeService
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DefaultObserver
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList
import javax.inject.Inject

/**
 * @author liuzeren
 * @time 2017/11/10    下午3:20
 * @mail lzr319@163.com
 */
class HomeFragmentPresenter @Inject constructor() : IHomeFragmentPresenter(){

    override fun initImage(imageView: ImageView, normalUrl: String?, selectedUrl: String?) {
        val drawable = StateListDrawable()
        imageView.setImageDrawable(drawable)

        Glide.with(imageView.context).load(normalUrl).asBitmap().into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                val bitmap = BitmapDrawable(resource)

                drawable.addState(intArrayOf(-android.R.attr.state_pressed, -android.R.attr.state_selected), bitmap)
                drawable.addState(intArrayOf(android.R.attr.state_pressed, -android.R.attr.state_selected), bitmap)
            }
        })
        Glide.with(imageView.context).load(selectedUrl).asBitmap().into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                val bitmap = BitmapDrawable(resource)

                drawable.addState(intArrayOf(-android.R.attr.state_pressed, android.R.attr.state_selected), bitmap)
                drawable.addState(intArrayOf(android.R.attr.state_pressed, android.R.attr.state_selected), bitmap)
            }
        })
    }

    override fun homeBottomBars() {
        mView?.showLoading()

        val request = BaseRequest<BaseUserRequestData>("catalogInfoProvider","queryBottombarInfo")
        request.data = BaseUserRequestData()

        mOkHttp.retrofit.builder<HomeService>(HomeService::class.java).queryBottombars(request).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).compose(RxLifecycleAndroid.bindFragment(lifecycleSubject))
                .concatMap { catalogInfo -> Observable.just(catalogInfo.resultData?.bottombarInfoList) }
                .subscribe(object : DefaultObserver<ArrayList<CatalogInfo>>() {

                    private var catalogInfos: ArrayList<CatalogInfo>? = null

                    override fun onNext(@io.reactivex.annotations.NonNull catalogInfos: ArrayList<CatalogInfo>) {
                        this.catalogInfos = catalogInfos
                    }

                    override fun onError(@io.reactivex.annotations.NonNull e: Throwable) {

                    }

                    override fun onComplete() {
                        if (null == catalogInfos || catalogInfos!!.size == 0) {
                            mView?.error(View.OnClickListener { homeBottomBars() })
                        } else {
                            mView?.hideLoading()
                            mView?.updateBottomBars(catalogInfos)
                        }
                    }
                })
    }
}