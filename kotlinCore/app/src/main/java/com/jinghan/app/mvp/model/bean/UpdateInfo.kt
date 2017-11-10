package com.jinghan.app.mvp.model.bean

/**
 * 升级信息体
 * @author liuzeren
 * @time 2017/11/10    下午3:29
 * @mail lzr319@163.com
 */
data class UpdateInfo(var version_code: Int = 0
                        ,var version_name: String? = null
                        ,var apkUrl: String? = null
                        ,var support_os_version: Int = 0
                        ,var apk_size: String? = null
                        ,var discription: String? = null
                        ,var updateType: Int = 0){//1无需升级 2建议升级 3强制升级

    companion object {

        /*强制升级*/
        val FORCE_UPDATE = 3

        /*建议升级*/
        val SUGGEST_UPDATE = 2
    }

}
