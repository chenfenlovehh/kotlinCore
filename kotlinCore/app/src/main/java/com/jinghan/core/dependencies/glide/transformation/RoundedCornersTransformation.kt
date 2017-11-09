package com.jh.sdk.dependencies.glide.transformations

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader

import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource

/**
 * Created by kwey on 2017/3/28.
 */

class RoundedCornersTransformation(private val mBitmapPool: BitmapPool, private val mRadius: Int) : Transformation<Bitmap> {

    constructor(context: Context, mRadius: Int) : this(Glide.get(context).bitmapPool, mRadius) {}

    override fun transform(resource: Resource<Bitmap>, outWidth: Int, outHeight: Int): Resource<Bitmap> {
        //从其包装类中拿出Bitmap
        val source = resource.get()
        val width = source.width
        val height = source.height
        var result: Bitmap? = mBitmapPool.get(width, height, Bitmap.Config.ARGB_8888)
        if (result == null) {
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(result!!)
        //以上已经算是教科书式写法了
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        canvas.drawRoundRect(RectF(0f, 0f, width.toFloat(), height.toFloat()), mRadius.toFloat(), mRadius.toFloat(), paint)
        //返回包装成Resource的最终Bitmap
        return BitmapResource.obtain(result, mBitmapPool)
    }

    override fun getId(): String {
        return "RoundedTransformation(radius=$mRadius)"
    }
}
