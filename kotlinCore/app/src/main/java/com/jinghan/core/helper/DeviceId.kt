package com.jinghan.core.helper

import android.Manifest
import android.content.Context
import android.provider.Settings
import android.provider.Settings.Secure
import android.text.TextUtils
import java.util.*

/**
 * @author liuzeren
 * @time 2017/11/3    下午2:53
 * @mail lzr319@163.com
 */
object DeviceId {

    /** Device Id在系统设置中存储的Key  */
    private val KEY_DEVICE_ID = "com.jinghan.iframe"

    /** 无读写权限时拼接Device Id所用前缀  */
    private val FAILSAFE_PREFIX = "com.jinghan"

    /**
     * 获取设备id。<br></br>
     *
     * 由于有些设备没有电话功能,所以不能依赖于 imei。
     * [Secure.ANDROID_ID][Secure]
     * 在 froyo以前版本也不可靠. 我们还需要考虑模拟器。<br></br>

     * @param context
     * *            Context
     * *
     * @return device id.
     */
    fun getDeviceID(context: Context): String {

        var deviceId = "" // 百度app android平台设备唯一标示.

        try {
            val hasSettingPermission = PermissionUtils.checkPermission(context, Manifest.permission.WRITE_SETTINGS)

            if (hasSettingPermission) {
                // 获取系统设置里的key
                deviceId = Settings.System.getString(context.contentResolver, KEY_DEVICE_ID)
            }

            // 如果为空，需要重新生成一个，然后写入设置。
            if (TextUtils.isEmpty(deviceId)) {

                deviceId = createDeviceId(context)

                if (hasSettingPermission) {
                    // 写入DeviceId
                    Settings.System.putString(context.contentResolver, KEY_DEVICE_ID, deviceId)
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = createDeviceId(context)
        }
        return deviceId
    }

    fun createDeviceId(context: Context): String {
        //        return MD5.encryptByMD5("110920" + getAndroidId(context));
        return getAndroidId(context)
    }

    /**
     * 获取设备 android id。[Secure.ANDROID_ID]

     * @param context
     * *            application context
     * *
     * @return 如果没有返回 “”空字符串
     */
    fun getAndroidId(context: Context): String {

        var androidId: String? = null
        try {
            // read android id
            androidId = Secure.getString(context.contentResolver, Secure.ANDROID_ID)

            if (TextUtils.isEmpty(androidId)) {
                androidId = createAndroidId()
                Secure.putString(context.contentResolver, Secure.ANDROID_ID, androidId)
            }
        } catch (t: Throwable) {
        }

        if (TextUtils.isEmpty(androidId)) {
            androidId = createAndroidId()
        }

        return androidId?:""
    }

    private fun createAndroidId(): String {
        return FAILSAFE_PREFIX + UUID.randomUUID().toString()
    }
}