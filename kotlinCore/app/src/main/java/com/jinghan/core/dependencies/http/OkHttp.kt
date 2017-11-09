package com.jinghan.core.dependencies.http

import android.content.Context
import android.text.TextUtils
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.jinghan.app.global.Constant
import com.jinghan.core.dependencies.http.interceptor.CacheInterceptor
import com.jinghan.core.dependencies.http.interceptor.HeaderInterceptor
import com.jinghan.core.dependencies.http.ssl.SSLHelper
import com.jinghan.core.helper.PermissionUtils
import com.orhanobut.logger.Logger
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author liuzeren
 * @time 2017/11/3    上午11:32
 * @mail lzr319@163.com
 */
@Singleton
class OkHttp @Inject constructor(context : Context){

    companion object {
        lateinit var instance : OkHttp

        @Synchronized fun getInstance(context: Context) : OkHttp{
            if(null == instance){
                instance = OkHttp(context)
            }

            return instance
        }
    }

    lateinit var mOkHttpClient : OkHttpClient

    init {
        val loggingInterceptor = HttpLoggingInterceptor{
            message -> Logger.i(message)
        }

        val builder = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
                .connectTimeout(Constant.HttpInfo.CONNETC_TIMEOUT,TimeUnit.SECONDS)
                .readTimeout(Constant.HttpInfo.READ_STREAM_TIMEOUT,TimeUnit.SECONDS)
                .writeTimeout(Constant.HttpInfo.WRITE_STREAM_TIMEOUT,TimeUnit.SECONDS)
                .connectionPool(ConnectionPool())
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(HeaderInterceptor(context))
                .addNetworkInterceptor(StethoInterceptor())

        if(Constant.HttpInfo.IS_CACHE) {
            builder.addNetworkInterceptor(CacheInterceptor(context))

            if(PermissionUtils.checkStoragePermissions(context)){//是否有SD读写权限，有的话则缓存在SD中，没有话则缓存在内部存储文件中
                builder.cache(Cache(File(Constant.HttpInfo.CACHE_PATH), Constant.HttpInfo.CACHE_SIZE))
            }else{
                builder.cache(Cache(File(context.cacheDir,"http"), Constant.HttpInfo.CACHE_SIZE))
            }
        }

        mOkHttpClient = builder.build()

        //判断是否在AppLication中配置Https证书
        if (!TextUtils.isEmpty(Constant.HttpInfo.SSL_NAME_IN_ASSETS)) {
            var inStream = context.assets.open(Constant.HttpInfo.SSL_NAME_IN_ASSETS)

            if(null != inStream) {
                mOkHttpClient = mOkHttpClient.newBuilder()
                        .sslSocketFactory(SSLHelper.getSslSocketFactory(inStream, null, ""))
                        .build()
            }
        }
    }

    inner class RetrofitClient(private var baseUrl:String = Constant.HttpInfo.BASE_URL){
        fun setBaseUrl(url: String):RetrofitClient{
            baseUrl = url
            return this
        }

        fun <T> builder(service:Class<T>):T{
            if (baseUrl == null) {
                throw RuntimeException("baseUrl is null!")
            }
            if (service == null) {
                throw RuntimeException("api Service is null!")
            }

            return Retrofit.Builder()
                    .client(mOkHttpClient)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(service)
        }
    }

    val retrofit : RetrofitClient get() = RetrofitClient()
}