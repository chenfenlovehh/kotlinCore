package com.jh.sdk.dependencies.glide.transformations

/**
 * Copyright (C) 2015 Wasabeef

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build

import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import com.jh.sdk.dependencies.glide.transformations.blur.FastBlur
import com.jh.sdk.dependencies.glide.transformations.blur.RSBlur


class BlurTransformation @JvmOverloads constructor(context: Context, private val mBitmapPool: BitmapPool = Glide.get(context).bitmapPool, private val mRadius: Int = MAX_RADIUS, private val mSampling: Int = DEFAULT_DOWN_SAMPLING) : Transformation<Bitmap> {

    private val mContext: Context

    constructor(context: Context, radius: Int) : this(context, Glide.get(context).bitmapPool, radius, DEFAULT_DOWN_SAMPLING) {}

    constructor(context: Context, radius: Int, sampling: Int) : this(context, Glide.get(context).bitmapPool, radius, sampling) {}

    init {
        mContext = context.applicationContext
    }

    override fun transform(resource: Resource<Bitmap>, outWidth: Int, outHeight: Int): Resource<Bitmap> {
        val source = resource.get()

        val width = source.width
        val height = source.height
        val scaledWidth = width / mSampling
        val scaledHeight = height / mSampling

        var bitmap: Bitmap? = mBitmapPool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap!!)
        canvas.scale(1 / mSampling.toFloat(), 1 / mSampling.toFloat())
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        canvas.drawBitmap(source, 0f, 0f, paint)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                bitmap = RSBlur.blur(mContext, bitmap, mRadius)
            } catch (e: RuntimeException) {
                bitmap = FastBlur.blur(bitmap, mRadius, true)
            }

        } else {
            bitmap = FastBlur.blur(bitmap, mRadius, true)
        }

        return BitmapResource.obtain(bitmap, mBitmapPool)
    }

    override fun getId(): String {
        return "BlurTransformation(radius=$mRadius, sampling=$mSampling)"
    }

    companion object {

        private val MAX_RADIUS = 25
        private val DEFAULT_DOWN_SAMPLING = 1
    }
}
