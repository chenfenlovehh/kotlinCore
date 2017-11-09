package com.jinghan.core.mvp.view.activity

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Build
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.CheckResult
import android.support.v7.app.AppCompatDelegate
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.jinghan.core.helper.PermissionUtils
import com.jinghan.core.mvp.view.impl.view.BaseView
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * @author liuzeren
 * @time 2017/11/6    下午2:08
 * @mail lzr319@163.com
 */
abstract class BaseActivity<B : ViewDataBinding> : DaggerAppCompatActivity(),LifecycleProvider<ActivityEvent>,BaseView{

    private val lifecycleSubject = BehaviorSubject.create<ActivityEvent>()
    protected lateinit var mViewBinding:B

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleSubject.onNext(ActivityEvent.CREATE)
        initContentView()
        initPresenter()
        initViewsAndListener()
        initToolbar()
        initData()
    }

    @CheckResult
    override fun lifecycle(): Observable<ActivityEvent> {
        return lifecycleSubject.hide()
    }

    @CheckResult
    override fun <T> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent<T, ActivityEvent>(lifecycleSubject, event)
    }

    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindActivity<T>(lifecycleSubject)
    }

    private fun initContentView(){
        mViewBinding = DataBindingUtil.setContentView<B>(this,layoutId)
    }

    protected abstract val layoutId:Int

    abstract fun initViewsAndListener()

    protected abstract fun initData()

    abstract fun initPresenter()

    abstract fun initToolbar()

    @CallSuper
    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(ActivityEvent.START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(ActivityEvent.RESUME)
    }

    @CallSuper
    override fun onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE)
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP)
        super.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY)
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        PermissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }

    override fun toast(txtId: Int) {
        toast(getString(txtId))
    }

    override fun toast(text: String) {
        if (TextUtils.isEmpty(text)) return

        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun close() {
        finish()
    }

    /**沉浸式 */
    protected fun initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val localLayoutParams = window.attributes
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags
        }
    }

    /**全屏*/
    protected fun fullScreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun error(listener: View.OnClickListener) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * * 显示或隐藏StatusBar

     * @param enable false 显示，true 隐藏
     */
    protected fun hideStatusBar(enable: Boolean) {
        val p = window.attributes
        if (enable)
        //|=：或等于，取其一
        {
            p.flags = p.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
        } else
        //&=：与等于，取其二同时满足，     ~ ： 取反
        {
            p.flags = p.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        }

        window.attributes = p
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    companion object {
        init {
            /**vector支持,向5.0之前提供支持 */
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}