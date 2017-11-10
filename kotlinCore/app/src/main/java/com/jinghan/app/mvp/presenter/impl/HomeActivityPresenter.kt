package com.jinghan.app.mvp.presenter.impl

import com.jinghan.app.mvp.presenter.IHomeActivityPresenter
import com.jinghan.core.R
import com.jinghan.core.mvp.view.impl.view.BaseView
import javax.inject.Inject

/**
 * @author liuzeren
 * @time 2017/11/10    下午2:15
 * @mail lzr319@163.com
 */
class HomeActivityPresenter @Inject constructor() : IHomeActivityPresenter(){

    private var mView : BaseView? = null

    private var mPressedTime: Long = 0

    override fun takeView(view: BaseView) {
    }

    override fun onBackPressed() {
        val mCurrentTime = System.currentTimeMillis()
        if (mCurrentTime - mPressedTime > 2000) {
            mView?.toast(R.string.exit)
            mPressedTime = mCurrentTime
        } else {
            mView?.close()
        }
    }

    override fun dropView() {
        mView = null
    }
}