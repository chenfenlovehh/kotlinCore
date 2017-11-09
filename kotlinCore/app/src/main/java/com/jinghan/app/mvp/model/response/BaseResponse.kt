package com.jinghan.app.mvp.model.response

/**
 * @author liuzeren
 * @time 2017/11/9    上午11:21
 * @mail lzr319@163.com
 */
open class BaseResponse<T>(var returnCode : String = ""
                           ,var message: String? = null
                           ,var resultData: T?=null){
    val isSuccess: Boolean
        get() = returnCode == STATUS_SUCCESS

    companion object {

        val STATUS_SUCCESS = "000000"
    }
}