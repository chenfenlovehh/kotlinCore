package com.jinghan.core.dependencies.databinding

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jh.sdk.dependencies.glide.transformations.CropCircleTransformation
import com.jh.sdk.dependencies.glide.transformations.RoundedCornersTransformation

/**
 * @author liuzeren
 * @time 2017/11/9    下午6:55
 * @mail lzr319@163.com
 */
object GlideBindingUtil {

    /**默认加载方式 */
    val GLIDE_IMAGE_DEFAULT = 0

    /**
     * 圆形加载方式
     * 使用圆形加载方式需要提供边框颜色和边框宽度
     * borderWidth 默认为0
     * borderColor 默认为白色
     */
    val GLIDE_IMAGE_CROPCIRCLE = 1

    /**
     * 图片圆角方式
     * 使用图片圆角的方式需要提供圆角半径大小
     * radius 圆角半径
     */
    val GLIDE_IMAGE_ROUNDEDCORNER = 2

    /**
     * @param imageType 图片加载方式,默认为0
     * *
     */
    @JvmStatic fun loadImage(imgView: ImageView,
                  url: String,
                  imageType: Int,
                  borderWidth: Int,
                  borderColor: Int,
                  radius: Int,
                  error: Drawable?,
                  placeholder: Drawable?) {

        val builder = Glide.with(imgView.context)
                .load(url)
                .error(error)
                .placeholder(placeholder)

        when (imageType) {
            GLIDE_IMAGE_DEFAULT -> {
            }
            GLIDE_IMAGE_CROPCIRCLE -> builder.bitmapTransform(CropCircleTransformation(imgView.context, borderWidth, borderColor))
            GLIDE_IMAGE_ROUNDEDCORNER -> builder.bitmapTransform(RoundedCornersTransformation(imgView.context, radius))
        }

        builder.into(imgView)
    }

    @BindingAdapter(value = *arrayOf("imageUrl", "error", "placeholder", "radius"), requireAll = true)
    @JvmStatic fun loadImage(imgView: ImageView,
                  url: String,
                  radius: Int,
                  error: Drawable,
                  placeholder: Drawable) {
        loadImage(imgView, url, radius, 0, 0, radius, error, placeholder)
    }

    @BindingAdapter(value = *arrayOf("imageUrl", "radius"), requireAll = true)
    @JvmStatic fun loadImage(imgView: ImageView,
                  url: String,
                  radius: Int) {
        loadImage(imgView, url, radius, 0, 0, radius, null, null)
    }

    @BindingAdapter(value = *arrayOf("imageUrl", "error", "placeholder"), requireAll = false)
    @JvmStatic fun loadImage(imgView: ImageView,
                  url: String,
                  error: Drawable,
                  placeholder: Drawable) {

        loadImage(imgView, url, GLIDE_IMAGE_DEFAULT, 0, 0, 0, error, placeholder)
    }

    @BindingAdapter(value = *arrayOf("imageUrl", "borderWidth", "borderColor", "error", "placeholder"), requireAll = true)
    @JvmStatic fun loadImage(imgView: ImageView,
                  url: String,
                  borderWidth: Int,
                  borderColor: Int,
                  error: Drawable,
                  placeholder: Drawable) {
        loadImage(imgView, url, GLIDE_IMAGE_CROPCIRCLE, borderWidth, borderColor, 0, error, placeholder)
    }

    @BindingAdapter(value = *arrayOf("res"))
    @JvmStatic fun loadImage(imgView: ImageView,
                  res: Drawable) {
        imgView.setImageDrawable(res)
    }

    @BindingAdapter(value = *arrayOf("imageUrl", "borderWidth", "borderColor"), requireAll = true)
    @JvmStatic fun loadImage(imgView: ImageView,
                  url: String,
                  borderWidth: Int,
                  borderColor: Int) {
        loadImage(imgView, url, GLIDE_IMAGE_CROPCIRCLE, borderWidth, borderColor, 0, null, null)
    }

    /**
     * 类型不匹配的问题，比如R.color.white是int，但是通过
     * Data Binding赋值给android:background属性后，需要
     * 把int转换为ColorDrawable
     */
    @BindingConversion
    @JvmStatic fun convertColorToDrawable(drawable: Int): Drawable {
        return ColorDrawable(drawable)
    }

    @BindingAdapter(value = *arrayOf("background"), requireAll = true)
    @JvmStatic fun loadImage(view: View,
                  background: Int) {
        view.setBackgroundResource(background)
    }
}