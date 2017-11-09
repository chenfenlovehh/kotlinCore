package com.jinghan.app.mvp.view.impl.api

import com.jinghan.app.mvp.model.request.BaseRequest
import com.jinghan.app.mvp.model.request.BaseUserRequestData
import com.jinghan.app.mvp.model.response.SplashResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author liuzeren
 * @time 2017/11/9    上午11:20
 * @mail lzr319@163.com
 */
interface SplashService{
    @POST("json")
    fun queryLoadingPageInfo(@Body request: BaseRequest<BaseUserRequestData>): Observable<SplashResponse>
}