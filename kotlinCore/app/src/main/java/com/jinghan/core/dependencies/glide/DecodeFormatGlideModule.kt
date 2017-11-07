package com.jinghan.core.dependencies.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.module.GlideModule

/**
 * 更改glide转默认的图片加载方式
 * glide默认的图片加载方式为RGB_565
 * 修改为ARGB_8888之后，内存使用将会提高很多，如果对图片要求不是特别高的应用，就不需要此项配置
 * @author liuzeren
 * @time 2017/11/6    上午9:44
 * @mail lzr319@163.com
 */
class DecodeFormatGlideModule : GlideModule{

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888)
    }

    override fun registerComponents(context: Context, glide: Glide) {}

}