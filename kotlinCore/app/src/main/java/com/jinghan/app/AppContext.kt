package com.jinghan.app

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.multidex.MultiDex
import com.facebook.stetho.Stetho
import com.jinghan.app.global.Constant
import com.jinghan.core.BuildConfig
import com.jinghan.core.dependencies.dragger2.component.DaggerAppComponent
import com.orhanobut.logger.*
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import java.util.*

/**
 * @author liuzeren
 * @time 2017/11/2    下午3:23
 * @mail lzr319@163.com
 */
class AppContext : DaggerApplication() {

    init {
        instance = this
    }

    companion object {
        lateinit var instance : AppContext
    }

    /**
     * 记录应用activity生命周期信息
     * */
    lateinit var store : Stack<Activity>

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        var appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this)
        return appComponent
    }

    override fun onCreate() {
        super.onCreate()

        LeakCanary.install(this)
        initStetho()
        initLogger(BuildConfig.DEBUG)
        registerLifecycle()
    }

    /**
     * 初始化stetho
     */
    private fun initStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build())
    }

    /**突破65535限制*/
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        MultiDex.install(this)
    }

    /**
     * 注册activity生命周期查看信息
     * */
    private fun registerLifecycle(){
        store = Stack()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {}

            override fun onActivityResumed(activity: Activity?) {}

            override fun onActivityStarted(activity: Activity?) {}

            override fun onActivityDestroyed(activity: Activity?) {
                store.remove(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

            override fun onActivityStopped(activity: Activity?) {}

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                store.add(activity)
            }
        })
    }

    /**当前activity*/
    fun currentActivity():Activity=store.lastElement()

    /**
     * 初始化日志信息
     * @param allowLog true记录日志
     * */
    private fun initLogger(allowLog:Boolean){
        Logger.clearLogAdapters()

        if(!allowLog){
            Logger.addLogAdapter(object : AndroidLogAdapter(){
                override fun isLoggable(priority: Int, tag: String?): Boolean {
                    return false
                }
            })
        }else{
            if(Constant.LogInfo.IS_WRITE_IN_LOCAL){
                val mLogStrategy = XDiskLogStrategy(mainLooper,Constant.LogInfo.LOG_PATH,Constant.LogInfo.LOG_FILE_SIZE)
                Logger.addLogAdapter(object : DiskLogAdapter(CsvFormatStrategy.newBuilder().logStrategy(mLogStrategy).tag(Constant.LogInfo.LOG_TAG).build()) {
                    override fun isLoggable(priority: Int, tag: String?): Boolean {
                        if(priority >= Constant.LogInfo.WRITE_LOCAL_LOG_LEVEL){
                            return true
                        }

                        return false
                    }
                })
            }

            //添加标准输出日志
            Logger.addLogAdapter(AndroidLogAdapter(PrettyFormatStrategy.newBuilder().tag(Constant.LogInfo.LOG_TAG).build()))
        }
    }

}