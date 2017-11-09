package com.jinghan.core.dependencies.http.interceptor

import com.jinghan.core.dependencies.http.model.UploadProgressListener
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.Response
import okio.*
import java.io.IOException

/**
 * 文件上传进度拦截器
 * @author liuzeren
 * @time 2017/11/3    下午5:18
 * @mail lzr319@163.com
 */
class UpLoadProgressInterceptor(private val mUploadListener: UploadProgressListener) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var body: RequestBody? = request.body()

        if(null == body) return chain.proceed(request)

        val build = request.newBuilder()
                .method(request.method(),
                        UploadProgressRequestBody(body))
                .build()
        return chain.proceed(build)
    }

    inner class UploadProgressRequestBody(private val mRequestBody: RequestBody) : RequestBody() {
        private var mCountingSink: CountingSink? = null

        override fun contentType(): MediaType? {
            return mRequestBody.contentType()
        }

        @Throws(IOException::class)
        override fun contentLength(): Long {
            try {
                return mRequestBody.contentLength()
            } catch (e: IOException) {
                e.printStackTrace()
                return -1
            }

        }

        @Throws(IOException::class)
        override fun writeTo(sink: BufferedSink) {
            val bufferedSink: BufferedSink

            mCountingSink = CountingSink(sink)
            bufferedSink = Okio.buffer(mCountingSink!!)

            mRequestBody.writeTo(bufferedSink)
            bufferedSink.flush()
        }

        internal inner class CountingSink(delegate: Sink) : ForwardingSink(delegate) {

            private var bytesWritten: Long = 0

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                bytesWritten += byteCount
                mUploadListener.onProgress(bytesWritten, contentLength())
            }
        }
    }
}