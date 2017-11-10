package com.jinghan.app.mvp.view.impl.api

import com.jinghan.app.mvp.model.request.BaseRequest
import com.jinghan.app.mvp.model.request.BaseUserRequestData
import com.jinghan.app.mvp.model.response.CatalogListResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author liuzeren
 * @time 2017/11/10    下午5:07
 * @mail lzr319@163.com
 */
interface HomeService{
    @POST("json")
    fun queryBottombars(@Body request: BaseRequest<BaseUserRequestData>): Observable<CatalogListResponse>
}