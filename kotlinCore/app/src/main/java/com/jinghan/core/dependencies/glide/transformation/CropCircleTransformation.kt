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
import android.graphics.*

import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource

class CropCircleTransformation(private val mBitmapPool: BitmapPool) : Transformation<Bitmap> {

    private var mBorderWidth = 0
    private var mBorderColor = 0

    constructor(context: Context) : this(Glide.get(context).bitmapPool) {}

    constructor(context: Context, borderWidth: Int) : this(context) {

        this.mBorderWidth = borderWidth
        this.mBorderColor = DEFAULT_BORDER_COLOR
    }

    constructor(context: Context, borderWidth: Int, borderColor: Int) : this(context) {

        this.mBorderWidth = borderWidth
        this.mBorderColor = borderColor
    }

    override fun transform(resource: Resource<Bitmap>, outWidth: Int, outHeight: Int): Resource<Bitmap> {
        val source = resource.get()
        val size = Math.min(source.width, source.height)

        val width = (source.width - size) / 2
        val height = (source.height - size) / 2

        var bitmap: Bitmap? = mBitmapPool.get(size, size, Bitmap.Config.ARGB_8888)
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap!!)
        val paint = Paint()
        val shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        if (width != 0 || height != 0) {
            // source isn't square, move viewport to center
            val matrix = Matrix()
            matrix.setTranslate((-width).toFloat(), (-height).toFloat())
            shader.setLocalMatrix(matrix)
        }
        paint.color = Color.WHITE
        paint.shader = shader
        paint.isAntiAlias = true

        val r = size / 2f

        if (mBorderWidth > 0) {
            val bgPaint = Paint()
            bgPaint.color = mBorderColor
            bgPaint.isAntiAlias = true
            canvas.drawCircle(r, r, r, bgPaint)
        }
        canvas.drawCircle(r, r, r - mBorderWidth, paint)
        //    canvas.drawCircle(r, r, r, paint);

        return BitmapResource.obtain(bitmap, mBitmapPool)
    }

    override fun getId(): String {
        return "CropCircleTransformation()"
    }

    companion object {

        private val DEFAULT_BORDER_WIDTH = 10
        private val DEFAULT_BORDER_COLOR = Color.parseColor("#ffffff")
    }
}
