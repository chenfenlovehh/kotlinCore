package com.jinghan.app.mvp.presenter.impl

import android.content.Context
import com.jinghan.app.mvp.presenter.ISplashActivityPresenter
import com.jinghan.core.dependencies.dragger2.scope.ActivityScoped
import com.jinghan.core.helper.AndroidUtils
import com.jinghan.core.mvp.view.impl.view.BaseView
import javax.inject.Inject

/**
 * @author liuzeren
 * @time 2017/11/8    下午2:17
 * @mail lzr319@163.com
 */
@ActivityScoped
class SplashActivityPresenter @Inject constructor(private val context:Context) : ISplashActivityPresenter {

    /**
     * 每个版本的第一次打开都应该显示引导页
     */
    override val isFirst: Boolean
        get() {
            val mSharedPreferences = context.getSharedPreferences(SPLASH_SHARED_IFRAME, Context.MODE_PRIVATE)

            val versionKey = String.format("%s_%d", KEY_FOR_VERSION_PREF, AndroidUtils.getVersionCode(context))

            val versionTimes = mSharedPreferences.getInt(versionKey, 1)
            val allTimes = mSharedPreferences.getInt(KEY_FOR_ALL, 1)

            val editor = mSharedPreferences.edit()
            editor.putInt(KEY_FOR_ALL, allTimes + 1)
            editor.putInt(versionKey, versionTimes + 1)
            editor.commit()

            return versionTimes == 1
        }

    override fun takeView(view: BaseView) {
    }

    override fun dropView() {
    }

    companion object {

        val SPLASH_SHARED_IFRAME = "splash_pref"

        /**
         * 总共打开的次数
         */
        val KEY_FOR_ALL = "open_times"

        /**
         * 记录每个版本打开的次数
         */
        val KEY_FOR_VERSION_PREF = "version_open_times"
    }
}