package com.jinghan.core.dependencies.http.interceptor

import android.content.Context
import android.os.Build
import com.jinghan.core.helper.AndroidUtils
import com.jinghan.core.helper.DeviceId
import okhttp3.Interceptor
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author liuzeren
 * @time 2017/11/3    下午2:26
 * @mail lzr319@163.com
 */
class HeaderInterceptor(private val mContext:Context) : Interceptor{
    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain!!.request()

        val builder = request.newBuilder()

        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyyMMddHHmmss")
        val opTime = sdf.format(c.time)
        builder.addHeader("op_time", opTime)
        builder.addHeader("channel_code", AndroidUtils.getChannel(mContext).toString())//11位渠道号，或与老大厅相似的字母类型渠道（要提供渠道对应关系）
        builder.addHeader("app_version", AndroidUtils.getVersionName(mContext))//客户端版本号
        builder.addHeader("platform", "1")//平台:IOS:2、Android:1
        builder.addHeader("os_version", Build.VERSION.RELEASE.toString())
        builder.addHeader("network_type", AndroidUtils.getNetworkType(mContext))//2G:1 3G:2 4G:3 WIFI:4其他:5

        /*if (MiGuLoginSDKHelper.getsInstance(mContext).isLogin()) {
            UserInfo userInfo = MiGuLoginSDKHelper.getsInstance(mContext).getUserInfo();

            builder.addHeader("user_id", String.valueOf(userInfo.getUserId()));//user_id
            builder.addHeader("tel", String.valueOf(userInfo.getPhone()));//手机号
            builder.addHeader("user_token", MiGuLoginSDKHelper.getsInstance(mContext).getUserToken());//用户token，非统一登录的token

            String passid = MiGuLoginSDKHelper.getsInstance(mContext).getIdentityId();
            builder.addHeader("passid", TextUtils.isEmpty(passid) ? "" : passid);//一级中心ID

            builder.addHeader("user_type", "3");//第三方账号：1 游客：2 手机登录3

            builder.addHeader("cookie", MiGuLoginSDKHelper.getsInstance(mContext).getUserToken());//cookie
            builder.addHeader("cookieHash", MiGuLoginSDKHelper.getsInstance(mContext).getUserToken());
        } else  {
            builder.addHeader("user_id", "0")//user_id
            builder.addHeader("tel", "")//手机号
            builder.addHeader("user_token", "")
            builder.addHeader("passid", "")

            builder.addHeader("user_type", "2")//第三方账号：1 游客：2 手机登录3

            builder.addHeader("cookie", "")//cookie
            builder.addHeader("cookieHash", "")
        }*/

        builder.addHeader("user_id", "0")//user_id
        builder.addHeader("tel", "")//手机号
        builder.addHeader("user_token", "")
        builder.addHeader("passid", "")

        builder.addHeader("user_type", "2")//第三方账号：1 游客：2 手机登录3

        builder.addHeader("cookie", "")//cookie
        builder.addHeader("cookieHash", "")













        builder.addHeader("imei", AndroidUtils.getDeviceId(mContext))//imei 号

        builder.addHeader("imsi", AndroidUtils.getIMSI(mContext))//imsi 号
        builder.addHeader("idfa", "")//idfa

        builder.addHeader("device_id", DeviceId.getAndroidId(mContext))
        //            builder.addHeader("device_id", "9990001");
        builder.addHeader("client_ip", AndroidUtils.hostIP)//客户端IP地址
        builder.addHeader("macaddress", AndroidUtils.getMacAddress(mContext))//mac地址

        builder.addHeader("brand", Build.BRAND)
        builder.addHeader("model", Build.MODEL)

        builder.addHeader("screen", String.format("%s*%s", AndroidUtils.getScreenWidth(mContext), AndroidUtils.getScreenHeight(mContext)))

        builder.addHeader("user_from", "1")//页面类型，App:1   H5:2
        builder.addHeader("provinceid", "")

        val r = Random(c.timeInMillis)
        val value = r.nextInt(900000) + 100000

        builder.addHeader("nonce_str", value.toString())//随机生成6位的数字字符串

        /*
        //MD5数字签名

        val sink = Buffer()

        request.body().let { it.writeTo(sink) }

        val sb = StringBuilder()
        sb.append(opTime).append(value.toString()).append(sink.readUtf8())
        builder.addHeader("sign", MD5.encryptByMD5(sb.toString()))

        */

        return chain.proceed(builder.build())
    }
}