package com.jinghan.core.dependencies.http.model

/**
 * 文件上传进度回调
 * @author liuzeren
 * @time 2017/11/3    下午5:15
 * @mail lzr319@163.com
 */
interface UploadProgressListener {

    /**
     * @param bytesWritten 文件总写入大小
     * @param contentLength 文件总大小
     * */
    fun onProgress(bytesWritten: Long, contentLength: Long)
}
