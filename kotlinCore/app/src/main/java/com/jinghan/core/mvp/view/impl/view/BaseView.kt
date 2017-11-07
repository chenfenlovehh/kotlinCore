package com.jinghan.core.mvp.view.impl.view

import android.view.View

/**
 * @author liuzeren
 * @time 2017/11/6    下午2:16
 * @mail lzr319@163.com
 */
interface BaseView {

    fun showLoading()

    fun hideLoading()

    fun error(listener: View.OnClickListener)

    fun toast(text: String)

    fun toast(txtId: Int)

    fun close()
}
