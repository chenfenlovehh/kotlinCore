package com.jinghan.app.global

import android.os.Environment
import com.orhanobut.logger.Logger
import java.io.File

/**
 * @author liuzeren
 * @time 2017/11/3    上午9:42
 * @mail lzr319@163.com
 */
object Constant{

    const val APP_NAME = "kotlin"

    /**
     * 应用输出根路径
     */
    val ROOT_PAHT = "${Environment.getExternalStorageDirectory().path}${File.separator}$APP_NAME${File.separator}"

    /**
     * apk更新下载所在路径
     */
    val APK_DOWNLOAD_PATH = "${ROOT_PAHT}${File.separator}apk${File.separator}"

    /**
     * 热修复补丁路径
     */
    val ANDFIX_PATCH_PATH = "{ROOT_PAHT}${File.separator}andfix${File.separator}"

    /**
     * http请求相关信息
     */
    object HttpInfo {
        /**
         * 请求根域名
         * baseUlr 必须以 /（斜线） 结束
         */
        val BASE_URL = ""

        /**
         * HTTPS证书名称
         * 私有证书，公有证书是不需要下载到本地的
         * */
        val SSL_NAME_IN_ASSETS = ""

        /**
         * 是否缓存请求
         */
        val IS_CACHE = true

        /**
         * 是否启用文件缓存请求
         */
        val IS_FILE_CACHE = true

        /**
         * 是否启用内存缓存请求
         */
        val IS_MEMORY_CACHE = true

        /**
         * 内存缓存时间
         * 单位（秒）
         * */
        val MEMORY_CACHE_TIME = 5 * 60

        /**
         * 磁盘缓存时间
         * 单位（秒）
         * */
        val DISK_CACHE_TIME = 60 * 60 * 24 * 28

        /**
         * 网络请求缓存路径
         */
        val CACHE_PATH = "$ROOT_PAHT${File.separator}net${File.separator}cache"

        /**
         * 网络请求缓存大小
         */
        val CACHE_SIZE = 10 * 1024 * 1024L

        /**
         * 网络连接超时时长
         * 单位（秒）
         * */
        val CONNETC_TIMEOUT = 10L

        /**
         * 读流超时时长
         * 单位（秒）
         * */
        val READ_STREAM_TIMEOUT = 15L

        /**
         * 写流超时时长
         * 单位（秒）
         * */
        val WRITE_STREAM_TIMEOUT = 20L
    }

    /**
     * 日志相关信息
     */
    object LogInfo {
        /**
         * 日志的tag标志
         */
        val LOG_TAG = "mvvm_dev"

        /**
         * 日志信息是否保存在本地
         */
        val IS_WRITE_IN_LOCAL = true

        /**
         * 日志输出路径
         */
        val LOG_PATH = String.format("%1\$s%2\$s", ROOT_PAHT, "log")

        /**
         * 单个日志文件的大小
         */
        val LOG_FILE_SIZE = 1 * 1024 * 1024

        /**
         * 写入文件的日志的level
         */
        val WRITE_LOCAL_LOG_LEVEL = Logger.ERROR
    }

    /**
     * 图片配置相关信息
     */
    object GlideInfo {

        /**
         * 图片缓存大小
         */
        val CACHE_SIZE = 100 * 1024 * 1024

        /**
         * 图片缓存路径
         */
        val CACHE_PATH = Constant.ROOT_PAHT + File.separator + "glide"
    }

}