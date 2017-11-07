package com.jinghan.core.mvp.preseneter

/**
 * @author liuzeren
 * @time 2017/11/6    下午2:12
 * @mail lzr319@163.com
 */
interface BasePresenter<T>{

    /**
     * Binds presenter with a view when resumed. The Presenter will perform initialization here.

     * @param view the view associated with this presenter
     */
    fun takeView(view:T)

    /**
     * Drops the reference to the view when destroyed
     */
    fun dropView()

}