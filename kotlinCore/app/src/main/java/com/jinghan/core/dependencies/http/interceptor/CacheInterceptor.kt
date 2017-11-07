package com.jinghan.core.dependencies.http.interceptor

import android.content.Context
import com.jinghan.app.global.Constant
import com.jinghan.core.helper.NetworkUtil
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author liuzeren
 * @time 2017/11/3    下午2:57
 * @mail lzr319@163.com
 */
class CacheInterceptor(private val mContext:Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        //post请求是不支持缓存的
        val isPost = "POST".equals(request.method(), ignoreCase = true)

        if (isPost) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build()
        } else {
            //断网后是否加载本地缓存数据
            if (!NetworkUtil.isNetworkAvailable(mContext)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
            }
        }

        val response = chain.proceed(request)
        //有网进行内存缓存数据
        if (response.isSuccessful) {
            //进行本地缓存数据，磁盘缓存优于内存缓存
            if (Constant.HttpInfo.IS_FILE_CACHE) {
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + Constant.HttpInfo.DISK_CACHE_TIME)
                        .removeHeader("Pragma")
                        .build()
            }else if(Constant.HttpInfo.IS_MEMORY_CACHE){
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + Constant.HttpInfo.MEMORY_CACHE_TIME)
                        .removeHeader("Pragma")
                        .build()
            }
        }

        return response
    }

}