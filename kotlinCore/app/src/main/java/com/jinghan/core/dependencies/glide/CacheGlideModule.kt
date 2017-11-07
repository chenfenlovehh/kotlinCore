package com.jinghan.core.dependencies.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.GlideModule
import com.jinghan.app.global.Constant
import com.jinghan.core.helper.PermissionUtils
import java.io.File

/**
 * 自定义glide的缓存机制
 * @author liuzeren
 * @time 2017/11/6    上午9:55
 * @mail lzr319@163.com
 */
class CacheGlideModule : GlideModule {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        customMemoryCache(context, builder)
        diskCache(context,builder)
    }

    override fun registerComponents(context: Context, glide: Glide) {
        // nothing to do here
    }

    /**
     * 自定义内存缓存
     */
    private fun customMemoryCache(context: Context, builder: GlideBuilder) {
        val calculator = MemorySizeCalculator(context)
        val defaultMemoryCacheSize = calculator.memoryCacheSize
        val defaultBitmapPoolSize = calculator.bitmapPoolSize

        val customMemoryCacheSize = (1.2 * defaultMemoryCacheSize).toInt()
        val customBitmapPoolSize = (1.2 * defaultBitmapPoolSize).toInt()

        builder.setMemoryCache(LruResourceCache(customMemoryCacheSize))
        builder.setBitmapPool(LruBitmapPool(customBitmapPoolSize))
    }

    private fun diskCache(context: Context,builder: GlideBuilder) {
        if(PermissionUtils.checkStoragePermissions(context)) {//如果没有SD卡读写权限，就无法在SD卡中缓存
            builder.setDiskCache(
                    DiskLruCacheFactory(Constant.GlideInfo.CACHE_PATH, Constant.GlideInfo.CACHE_SIZE)
            )
        }else{
            builder.setDiskCache(DiskLruCacheFactory(File(context.cacheDir,"glide").absolutePath, Constant.GlideInfo.CACHE_SIZE))
        }
    }
}