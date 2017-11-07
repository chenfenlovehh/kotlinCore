package com.jinghan.core.dependencies.http.model

/**
 * 文件下载进度
 * @author liuzeren
 * @time 2017/11/3    下午5:15
 * @mail lzr319@163.com
 */
interface DownloadProgressListener{

    /**
     * @param 已下载大小
     * @param contentLength 文件总大小
     * @param done 是否成功
     * */
    fun update(bytesRead: Long, contentLength: Long, done: Boolean)
}