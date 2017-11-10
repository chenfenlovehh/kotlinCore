package com.jinghan.app.mvp.model.response

import com.jinghan.app.mvp.model.bean.CatalogInfo
import java.util.ArrayList

/**
 * @author liuzeren
 * @time 2017/11/10    下午5:08
 * @mail lzr319@163.com
 */
class CatalogListResponse : BaseResponse<CatalogListResponse.Data>() {

    data class Data(var bottombarInfoList: ArrayList<CatalogInfo>? = null)

}