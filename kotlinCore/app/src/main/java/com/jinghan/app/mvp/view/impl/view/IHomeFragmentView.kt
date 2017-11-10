package com.jinghan.app.mvp.view.impl.view

import com.jinghan.app.mvp.model.bean.CatalogInfo
import com.jinghan.core.mvp.view.impl.view.BaseView
import java.util.ArrayList

/**
 * @author liuzeren
 * @time 2017/11/10    下午3:18
 * @mail lzr319@163.com
 */
interface IHomeFragmentView : BaseView{

    /**
     * 更新底部栏信息
     * */
    fun updateBottomBars(catalogInfos: ArrayList<CatalogInfo>?)

}