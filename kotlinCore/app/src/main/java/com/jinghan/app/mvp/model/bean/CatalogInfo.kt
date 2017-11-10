package com.jinghan.app.mvp.model.bean

/**
 * 底部栏信息
 * @author liuzeren
 * @time 2017/11/10    下午4:11
 * @mail lzr319@163.com
 */
data class CatalogInfo(var catalogId: Long = 0
                       ,var catalogName: String? = null
                       ,var logo: String? = null
                       ,var selectedLogo: String? = null
                       ,var service: String? = null
                       ,var method: String? = null
                       ,var oraderflag: Long = 0
                       ,var jumpType: String? = null
                       ,var catalogType: String? = null
                       ,var parentId: Long = 0
                       ,var highLight: String? = null)