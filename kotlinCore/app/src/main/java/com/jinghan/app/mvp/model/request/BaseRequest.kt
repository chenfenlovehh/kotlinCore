package com.jinghan.app.mvp.model.request

/**
 * @author liuzeren
 * @time 2017/11/9    上午11:13
 * @mail lzr319@163.com
 */
data class BaseRequest<T:BaseRequestData>(var service: String//请求的类名
                                     ,var method: String//请求的方法名
                                     ,var version:String = "1.0.0"//服务器端接口版本号,默认为1.0.0
                                     ,var data:T? = null)//封装参数对象