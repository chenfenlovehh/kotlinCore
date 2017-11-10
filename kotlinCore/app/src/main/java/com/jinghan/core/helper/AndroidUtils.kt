package com.jinghan.core.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.*
import android.preference.PreferenceManager
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import java.io.*
import java.lang.Exception
import java.lang.NumberFormatException
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * @author liuzeren
 * @time 2017/11/3    下午2:52
 * @mail lzr319@163.com
 */
object AndroidUtils {
    /**
     * Convert a translucent themed Activity
     * [android.R.attr.windowIsTranslucent] to a fullscreen opaque
     * Activity.
     *
     *
     * Call this whenever the background of a translucent Activity has changed
     * to become opaque. Doing so will allow the [android.view.Surface] of
     * the Activity behind to be released.
     *
     *
     * This call has no effect on non-translucent activities or on activities
     * with the [android.R.attr.windowIsFloating] attribute.
     */
    fun convertActivityFromTranslucent(activity: Activity) {
        try {
            val method = Activity::class.java!!.getDeclaredMethod("convertFromTranslucent")
            method.setAccessible(true)
            method.invoke(activity)
        } catch (t: Throwable) {
        }

    }

    /**
     * Calling the convertToTranslucent method on platforms after Android 5.0
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private fun convertActivityToTranslucentAfterL(activity: Activity) {
        try {
            val getActivityOptions = Activity::class.java!!.getDeclaredMethod("getActivityOptions")
            getActivityOptions.setAccessible(true)
            val options = getActivityOptions.invoke(activity)

            val classes = Activity::class.java!!.getDeclaredClasses()
            var translucentConversionListenerClazz: Class<*>? = null
            for (clazz in classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz
                }
            }
            val convertToTranslucent = Activity::class.java!!.getDeclaredMethod("convertToTranslucent",
                    translucentConversionListenerClazz, ActivityOptions::class.java)
            convertToTranslucent.setAccessible(true)
            convertToTranslucent.invoke(activity, null, options)
        } catch (t: Throwable) {
        }

    }

    /**
     * 获取设备号（imei）

     * @param context
     * *
     * @return
     */
    @SuppressLint("MissingPermission")
    fun getDeviceId(context: Context): String {
        var deviceId = "000000000000"
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            deviceId = tm.deviceId
        } catch (e: Exception) {
        }

        return deviceId
    }

    /**
     * 获取手机IMSI号
     */
    fun getIMSI(context: Context): String {
        var imsi = ""
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            imsi = tm.subscriberId
        } catch (e: Exception) {
        } finally {
            if (TextUtils.isEmpty(imsi)) {
                imsi = ""
            }
        }

        return imsi
    }

    /**
     * 获取设备的mac地址

     * @param context
     * *
     * @return 返回设备mac
     */
    fun getMacAddress(context: Context): String {
        try {
            val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val info = wifi.connectionInfo
            return if (info == null) "" else info.macAddress
        } catch (e: Exception) {
        }

        return ""
    }

    /**
     * @param context
     * *
     * @return 返回网络是否可用
     */
    fun isNetworkAvailable(context: Context?): Boolean {
        /* if (context == null)
            return false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info[] = manager.getAllNetworkInfo();
        for (int i = 0; i < info.length; i++) {
            NetworkInfo net = info[i];
            if (net.getTypeName().equalsIgnoreCase("WIFI") && net.isConnected()) {//忽略大小写
                return true;
            } else if (net.getTypeName().equalsIgnoreCase("mobile") && net.isConnected()) {
                return true;
            }
        }
        return false;*/
        if (context != null) {
            val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable
            }
        }
        return false
    }

    /**
     * @param context
     * *
     * @param dpValue
     * *
     * @return 将dp转换成px
     */
    fun dip2px(context: Context, dpValue: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun dip2px(context: Context, dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dpValue * scale + 0.5f
    }

    fun sp2px(context: Context, spValue: Float): Int {
        return (context.resources.displayMetrics.scaledDensity * spValue + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * @return 设备版本号
     */
    val systemVersion: String
        get() = Build.VERSION.RELEASE

    /**
     * 获取版本号名称

     * @param context
     * *
     * @param packageName
     * *
     * @return 应用的版本号
     */
    @JvmOverloads fun getVersionName(context: Context, packageName: String = context.packageName): String {
        val manager = context.packageManager
        val info: PackageInfo
        try {
            info = manager.getPackageInfo(packageName, 0)
            return info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
    }

    /**
     * 获取版本号

     * @param context
     * *
     * @param packageName
     * *
     * @return 当前应用的版本号
     */
    @JvmOverloads fun getVersionCode(context: Context, packageName: String = context.packageName): Int {
        val manager = context.packageManager
        val info: PackageInfo
        try {
            info = manager.getPackageInfo(packageName, 0)
            return info.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return -1
    }

    /**
     * 是否需要更新

     * @param context
     * *
     * @param packageName
     * *
     * @param newVersionName
     * *
     * @return
     */
    fun needUpdate(context: Context, packageName: String, newVersionName: String): Boolean {
        try {
            val curVersionName = getVersionName(context, packageName)
            val newVersions = newVersionName.split("\\.".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val curVersions = curVersionName.split("\\.".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val nl = newVersionName.length
            val cl = curVersionName.length
            val len = if (nl < cl) nl else cl
            for (i in 0..len - 1) {
                val n = newVersions[i]
                val c = curVersions[i]
                if (TextUtils.isDigitsOnly(n) && TextUtils.isDigitsOnly(c) &&
                        Integer.parseInt(n) > Integer.parseInt(c)) {
                    return true
                }
            }
        } catch (t: Throwable) {
        }

        return false
    }

    /**
     * 是否需要更新

     * @param context
     * *
     * @param packageName
     * *
     * @param newVersionCode
     * *
     * @return
     */
    fun newNeedUpdate(context: Context, packageName: String, newVersionCode: String): Boolean {
        try {
            val curVersionCode = getVersionCode(context, packageName)
            return Integer.parseInt(newVersionCode) > curVersionCode
        } catch (t: Throwable) {
        }

        return false
    }

    /**
     * 获取metaData。

     * @param act
     * *
     * @return
     */
    fun getApplicaitonMetaData(act: Application): Bundle? {
        try {
            val appInfo = act.packageManager.getApplicationInfo(act.packageName,
                    PackageManager.GET_META_DATA)
            return appInfo.metaData
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    fun getMetaData(context: Context, key: String): Any? {
        try {
            val appInfo = context.packageManager.getApplicationInfo(context.packageName,
                    PackageManager.GET_META_DATA)
            return appInfo.metaData.get(key)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * @return 判断sd卡是否挂载好
     */
    val isSDCardMounted: Boolean
        get() {
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                return true
            } else {
                return false
            }
        }

    /**
     * 隐藏键盘

     * @param context
     * *
     * @since 隐藏输入法
     */
    fun hideInputMethod(context: Activity?): Boolean {
        if (context == null) {
            return false
        }
        val currentFocus = context.currentFocus
        if (currentFocus != null) {
            val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return manager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
        return false
    }

    fun hideInputMethod(context: Context?, currentFocus: IBinder?): Boolean {
        if (context == null) {
            return false
        }

        if (currentFocus != null) {
            val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return manager.hideSoftInputFromWindow(currentFocus, InputMethodManager.HIDE_NOT_ALWAYS)
        }
        return false
    }

    /**
     * 显示键盘

     * @param context
     * *
     * @param view
     * *
     * @since 显示软键盘
     */
    fun showInputMethod(context: Activity, view: View) {
        val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.showSoftInput(view, 0)
    }

    /**
     * 获取屏幕宽度

     * @param context
     * *
     * @return
     */
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高度

     * @param context
     * *
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /*public static Size getScreenSize(Context context) {
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return new Size(metrics.widthPixels, metrics.heightPixels);
    }*/

    /**
     * 通过包名检测系统中是否安装某个应用程序

     * @param context
     * *
     * @param packageName
     * *
     * @return
     */
    fun checkApkExist(context: Context, packageName: String): Boolean {
        if (TextUtils.isEmpty(packageName)) {
            return false
        }
        try {
            context.packageManager.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }

    }

    /**
     * 获取文件大小

     * @param f
     * *
     * @return
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getFileSize(f: File): Long {
        var size: Long = 0
        val flist = f.listFiles()
        for (i in flist.indices) {
            val subFile = flist[i]
            if (subFile.isDirectory) {
                size = size + getFileSize(subFile)
            } else {
                size = size + subFile.length()
            }
        }
        return size
    }


    fun saveBitmapToFile(bitmap: Bitmap, path: String, format: Bitmap.CompressFormat): Boolean {
        var ret = false
        val file = File(path)
        if (!bitmap.isRecycled) {
            try {
                ret = bitmap.compress(format, 80, FileOutputStream(file, false))
            } catch (e: Exception) {
            }

            if (!ret) {
                file.delete()
            }
        }
        return ret
    }

    fun setTextViewLeftDrawable(context: Context, tv: TextView, id: Int) {
        val img = context.resources.getDrawable(id)
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img.setBounds(0, 0, img.minimumWidth, img.minimumHeight)
        tv.setCompoundDrawables(img, null, null, null) //设置左图标
    }


    fun installApp(context: Context, appFile: File) {
        if (appFile.exists()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)//service中启动activity
            intent.setDataAndType(Uri.fromFile(appFile),
                    "application/vnd.android.package-archive")
            context.startActivity(intent)
        }
    }

    fun getAppPackageName(context: Context, appName: String): String? {
        val mAppList = context.packageManager.getInstalledApplications(0)
        for (item in mAppList) {
            if (item.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                if (item.loadLabel(context.packageManager).toString()
                        .contains(appName)) {
                    return item.packageName
                }
            } else {

            }
        }
        return null
    }

    fun openApp(context: Context, appPackageName: String) {
        val resolveIntent = Intent(Intent.ACTION_MAIN, null)
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        resolveIntent.`package` = appPackageName
        val resolveInfoList = context.packageManager
                .queryIntentActivities(resolveIntent, 0)
        if (resolveInfoList != null && resolveInfoList.size > 0) {
            val resolveInfo = resolveInfoList[0]
            val activityPackageName = resolveInfo.activityInfo.packageName
            val className = resolveInfo.activityInfo.name

            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val componentName = ComponentName(
                    activityPackageName, className)

            intent.component = componentName
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    fun openMiGuChessApp(context: Context, appPackageName: String, mainActivity: String) {
        val resolveIntent = Intent(Intent.ACTION_MAIN, null)
        resolveIntent.`package` = appPackageName
        val resolveInfoList = context.packageManager
                .queryIntentActivities(resolveIntent, 0)
        if (resolveInfoList != null && resolveInfoList.size > 0) {
            val resolveInfo = resolveInfoList[0]
            val activityPackageName = appPackageName
            val className = mainActivity
            val intent = Intent(Intent.ACTION_MAIN)
            val componentName = ComponentName(
                    activityPackageName, className)
            intent.component = componentName
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    /**
     * 获取apk包名

     * @param context
     * *
     * @param apkPath
     * *
     * @return
     */
    fun getPackageName(context: Context, apkPath: String): String? {
        val pm = context.packageManager
        val info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES)
        if (info != null) {
            val appInfo = info.applicationInfo
            if (appInfo != null) {
                return appInfo.packageName
            }
        }
        return null
    }

    /**
     * 判断当前网络是否已经连接，并且是2G状态

     * @param ctx
     * *
     * @return
     */
    fun is2GMobileNetwork(ctx: Context): Boolean {
        val manager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info: NetworkInfo?
        try {
            info = manager.activeNetworkInfo
            if (info != null && info.type == ConnectivityManager.TYPE_MOBILE) {
                val currentNetworkType = info.subtype
                return true
            }
        } catch (e: Exception) {
        }

        return false
    }

    fun isWifiNetwork(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info: NetworkInfo?
            try {
                info = mConnectivityManager.activeNetworkInfo
                if (info != null && info.type == ConnectivityManager.TYPE_WIFI) {
                    return true
                }
            } catch (e: Exception) {
                return false
            }

        }
        return false
    }

    /**
     * 获取状态栏高度

     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var c: Class<*>? = null
        var obj: Any? = null
        var field: java.lang.reflect.Field? = null
        var x = 0
        var statusBarHeight = 0
        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c!!.newInstance()
            field = c.getField("status_bar_height")
            x = Integer.parseInt(field!!.get(obj).toString())
            statusBarHeight = context.resources.getDimensionPixelSize(x)
            return statusBarHeight
        } catch (e: Exception) {
            e.printStackTrace()
        }

        statusBarHeight = dip2px(context, 25)
        return statusBarHeight
    }

    /**
     * 获取渠道号

     * @param context
     * *
     * @return
     */
    fun getChannel(context: Context): Long {

        var iChannel: Long = 1000100

        val appinfo = context.applicationInfo
        val sourceDir = appinfo.sourceDir
        var zipfile: ZipFile? = null
        val start_flag = "META-INF/CPAChannel.txt"
        try {
            zipfile = ZipFile(sourceDir)
            val entries = zipfile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement() as ZipEntry
                val entryName = entry.name
                if (entryName == start_flag) {
                    val channel = inputStreamToString(zipfile.getInputStream(entry))
                    //                    String channel = entryName.replaceAll(start_flag, "");

                    try {
                        iChannel = java.lang.Long.parseLong(channel)
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }

                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return iChannel
    }

    /**
     * 截屏

     * @param activity
     * *
     * @return
     */

    fun captureScreen(activity: Activity): Bitmap {

        activity.window.decorView.isDrawingCacheEnabled = true

        val bmp = activity.window.decorView.drawingCache

        return bmp

    }

    /**
     * 是否是第一次使用软件

     * @return true:首次安装 false:升级了
     */
    fun isFirstUse(context: Context): Int {
        try {
            val info = context.packageManager.getPackageInfo(context.packageName, 0)
            val curVersion = info.versionCode
            // SettingUtils.setEditor(context, "version", paramString2);
            // int lastVersion = SettingUtils.getSharedPreferences(context, "version", 0);
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            val lastVersion = sp.getInt("version", 0)
            if (curVersion > lastVersion && lastVersion == 0) {
                // 如果当前版本大于上次版本，该版本属于第一次启动
                // 将当前版本写入preference中，则下次启动的时候，据此判断，不再为首次启动
                return 1// 首次安装
            } else {
                if (curVersion != lastVersion) {
                    Log.i("TTT", " 升级 curVersion  " + curVersion)
                    return 2// 升级
                } else {
                    Log.i("TTT", " 不升级 curVersion  " + curVersion)
                    return 0// 正常安装
                }

            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.i("TTT", " isFirstUse e " + e.toString())
        }

        return 0// 正常安装
    }

    /**
     * setAPPUsed:设置APP已经使用过了. <br></br>

     * @author wangheng
     */
    fun setAPPUsed(context: Context) {
        try {
            val info = context.packageManager.getPackageInfo(context.packageName, 0)
            val curVersion = info.versionCode
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            sp.edit().putInt("version", curVersion).commit()
            // SettingUtils.setEditor(context, "version", curVersion);
        } catch (e: PackageManager.NameNotFoundException) {
            Log.i("TTT", " setAPPUsed e " + e.toString())
        }

    }


    fun inputStreamToString(`is`: InputStream): String {

        var s = ""
        var line = ""
        val rd = BufferedReader(InputStreamReader(`is`))
        try {
            while ({ line = rd.readLine(); line }() != null) {
                s += line
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return s
    }

    fun lengthByChar(s: String?): Int {
        if (s == null) {
            return 0
        }
        val c = s.toCharArray()
        var len = 0
        for (i in c.indices) {
            len++
            if (!isLetter(c[i])) {
                len++
            }
        }
        return len
    }

    private fun isLetter(c: Char): Boolean {
        return c.toInt() / ASCII_UPPER_LIMIT == 0
    }

    private val ASCII_UPPER_LIMIT = 0x80

    // 得到本机Mac地址
    fun getLocalMac(context: Context): String {
        // 获取wifi管理器
        val wifiMng = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfor = wifiMng.connectionInfo
        return wifiInfor.macAddress
    }

    /**
     * 获取ip地址

     * @return
     */
    val hostIP: String?
        get() {
            var hostIp: String? = null
            try {
                val nis = NetworkInterface.getNetworkInterfaces()
                var ia: InetAddress? = null
                while (nis.hasMoreElements()) {
                    val ni = nis.nextElement()
                    val ias = ni.inetAddresses
                    while (ias.hasMoreElements()) {
                        ia = ias.nextElement()
                        if (ia is Inet6Address) {
                            continue
                        }

                        val ip = ia!!.hostAddress
                        if ("127.0.0.1" != ip) {
                            hostIp = ia.hostAddress
                            break
                        }
                    }
                }
            } catch (e: SocketException) {
                e.printStackTrace()
            }

            return hostIp
        }

    /**
     * 2G:1 3G:2 4G:3 WIFI:4其他:5
     */
    fun getNetworkType(context: Context): String {
        var strNetworkType = "5"


        val mConnectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo: NetworkInfo? = null
        try {
            networkInfo = mConnectivityManager.activeNetworkInfo
        } catch (e: Exception) {
        }

        if (networkInfo != null && networkInfo.isConnected) {
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "4"
            } else if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                val _strSubTypeName = networkInfo.subtypeName

                // TD-SCDMA   networkType is 17
                val networkType = networkInfo.subtype
                when (networkType) {
                    TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN //api<8 : replace by 11
                    -> strNetworkType = "1"
                    TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B //api<9 : replace by 14
                        , TelephonyManager.NETWORK_TYPE_EHRPD  //api<11 : replace by 12
                        , TelephonyManager.NETWORK_TYPE_HSPAP  //api<13 : replace by 15
                    -> strNetworkType = "2"
                    TelephonyManager.NETWORK_TYPE_LTE    //api<11 : replace by 13
                    -> strNetworkType = "3"
                    else ->
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equals("TD-SCDMA", ignoreCase = true) || _strSubTypeName.equals("WCDMA", ignoreCase = true) || _strSubTypeName.equals("CDMA2000", ignoreCase = true)) {
                            strNetworkType = "2"
                        } else if (_strSubTypeName.equals("TD-LTE_CA", ignoreCase = true)) {
                            strNetworkType = "3"
                        } else {
                            strNetworkType = "5"
                        }
                }
            }
        }

        return strNetworkType
    }

    fun setSystemBarColor(activity: Activity?, color: Int) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (null != activity) {
                activity.window.statusBarColor = color
            }

        }
    }

    /**
     * The `fragment` is added to the container view with id `frameId`. The operation is
     * performed by the `fragmentManager`.

     */
    fun addFragmentToActivity(fragmentManager: FragmentManager,
                              fragment: Fragment, frameId: Int) {
        if(fragment.isAdded) return

        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()
    }

    /**
     * The `fragment` is added to the container view with id `frameId`. The operation is
     * performed by the `fragmentManager`.

     */
    fun addFragmentToActivity(fragmentManager: FragmentManager,
                              fragment: Fragment, tag: String) {
        val transaction = fragmentManager.beginTransaction()
        transaction.add(fragment, tag)
        transaction.commit()
    }

    /**
     * 检查SD卡是否存在
     */
    private fun checkSdCard(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }


    /**
     * 获取手机SD卡总空间
     */
    private val sDcardTotalSize: Long
        get() {
            if (checkSdCard()) {
                val path = Environment.getExternalStorageDirectory()
                val mStatFs = StatFs(path.path)
                val blockSizeLong = mStatFs.blockSizeLong
                val blockCountLong = mStatFs.blockCountLong
                return blockSizeLong * blockCountLong
            } else {
                return 0
            }
        }


    /**
     * 获取SDka可用空间
     */
    private val sDcardAvailableSize: Long
        get() {
            if (checkSdCard()) {
                val path = Environment.getExternalStorageDirectory()
                val mStatFs = StatFs(path.path)
                val blockSizeLong = mStatFs.blockSizeLong
                val availableBlocksLong = mStatFs.availableBlocksLong
                return blockSizeLong * availableBlocksLong
            } else {
                return 0
            }
        }


    /**
     * 获取手机内部存储总空间
     */
    val phoneTotalSize: Long
        get() {
            if (!checkSdCard()) {
                val path = Environment.getDataDirectory()
                val mStatFs = StatFs(path.path)
                val blockSizeLong = mStatFs.blockSizeLong
                val blockCountLong = mStatFs.blockCountLong
                return blockSizeLong * blockCountLong
            } else {
                return sDcardTotalSize
            }
        }


    /**
     * 获取手机内存存储可用空间
     */
    val phoneAvailableSize: Long
        get() {
            if (!checkSdCard()) {
                val path = Environment.getDataDirectory()
                val mStatFs = StatFs(path.path)
                val blockSizeLong = mStatFs.blockSizeLong
                val availableBlocksLong = mStatFs.availableBlocksLong
                return blockSizeLong * availableBlocksLong
            } else
                return sDcardAvailableSize
        }

    fun installUpdateApp(context: Context, downloadUrl: String) {
        val appFile = File(downloadUrl)
        if (appFile.exists()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)//service中启动activity
            intent.setDataAndType(Uri.fromFile(appFile),
                    "application/vnd.android.package-archive")
            context.startActivity(intent)
        }
    }
}