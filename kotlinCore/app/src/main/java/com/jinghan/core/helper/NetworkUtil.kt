package com.jinghan.core.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import java.lang.Exception

/**
 * @author liuzeren
 * @time 2017/11/3    下午3:02
 * @mail lzr319@163.com
 */
object NetworkUtil{
    val CHINA_MOBILE = 1 // 中国移动
    val CHINA_UNICOM = 2 // 中国联通
    val CHINA_TELECOM = 3 // 中国电信


    val SIM_OK = 0
    val SIM_NO = -1
    val SIM_UNKNOW = -2

    val CONN_TYPE_WIFI = "wifi"
    val CONN_TYPE_GPRS = "gprs"
    val CONN_TYPE_NONE = "none"

    /**
     * 判断网络连接有效

     * @return 判断网络连接有效
     */
    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        val manager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val infos = manager.allNetworkInfo
        for (info in infos) {
            if (info.isConnected) {
                return true
            }
        }
        return false
    }

    /**
     * 判断当前网络是否是wifi网络

     * @param context 上下文
     * *
     * @return boolean 是否是wifi网络
     */
    fun isWifi(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        if (activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_WIFI) {
            return true
        }
        return false
    }

    /**
     * 判断当前网络是否是3g网络

     * @param context 上下文
     * *
     * @return 是否是3g网络
     */
    fun is3G(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        if (activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_MOBILE) {
            return true
        }
        return false
    }

    /**
     * 取得网络类型，wifi 2G 3G

     * @param context 上下文
     * *
     * @return WF 2G 3G 4G，或空 如果没网
     */
    fun getWifiOr2gOr3G(context: Context?): String {
        var networkType = ""
        if (context != null) {
            try {
                val cm = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetInfo = cm.activeNetworkInfo
                if (activeNetInfo != null && activeNetInfo.isConnectedOrConnecting) { // 有网
                    networkType = activeNetInfo.typeName.toLowerCase()
                    if (networkType == "wifi") {
                        networkType = "WF"
                    } else { // 移动网络
                        // //如果使用移动网络，则取得apn
                        // apn = activeNetInfo.getExtraInfo();
                        // 将移动网络具体类型归一化到2G 3G 4G
                        networkType = "2G" // 默认是2G
                        val subType = activeNetInfo.subtype
                        when (subType) {
                            TelephonyManager.NETWORK_TYPE_1xRTT -> networkType = "3G"
                            TelephonyManager.NETWORK_TYPE_CDMA // IS95
                            -> {
                            }
                            TelephonyManager.NETWORK_TYPE_EDGE // 2.75
                            -> {
                            }
                            TelephonyManager.NETWORK_TYPE_EVDO_0 -> networkType = "3G"
                            TelephonyManager.NETWORK_TYPE_EVDO_A -> networkType = "3G"
                            TelephonyManager.NETWORK_TYPE_GPRS // 2.5
                            -> {
                            }
                            TelephonyManager.NETWORK_TYPE_HSDPA // 3.5
                            -> networkType = "3G"
                            TelephonyManager.NETWORK_TYPE_HSPA // 3.5
                            -> networkType = "3G"
                            TelephonyManager.NETWORK_TYPE_HSUPA -> networkType = "3G"
                            TelephonyManager.NETWORK_TYPE_UMTS -> networkType = "3G"
                            TelephonyManager.NETWORK_TYPE_EHRPD -> networkType = "3G"
                            TelephonyManager.NETWORK_TYPE_EVDO_B -> networkType = "3G"
                            TelephonyManager.NETWORK_TYPE_HSPAP -> networkType = "3G"
                            TelephonyManager.NETWORK_TYPE_IDEN -> {
                            }
                            TelephonyManager.NETWORK_TYPE_LTE -> networkType = "4G"
                            else -> {
                            }
                        }// ~ 1-2 Mbps
                        // ~ 5 Mbps
                        // ~ 10-20 Mbps
                        // ~25 kbps
                        // ~ 10+ Mbps
                    } // end 移动网络if
                } // end 有网的if
            } catch (e: Exception) {
                e.printStackTrace()
                // TODO: handle exception
            }

        }
        return networkType
    }

    /**
     * 程序启动时判断活动状态的网络类型

     * @param context 上下文
     * *
     * @return 网络类型
     */
    fun getNetworkType(context: Context): String? {
        var result: String? = null


        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (connectivity == null) {
            result = null
        } else {

            val info = connectivity.allNetworkInfo

            if (info != null) {
                for (i in info.indices) {
                    if (info[i] != null) {
                        val tem = info[i].state
                        if (tem == NetworkInfo.State.CONNECTED || tem == NetworkInfo.State.CONNECTING) {
                            val temp = info[i].extraInfo
                            result = info[i].typeName + " " + info[i].subtypeName + temp
                            break
                        }
                    }
                }
            }

        }

        return result
    }

    /**
     * 获得网络连接类型

     * @param context 上下文
     * *
     * @return 网络连接类型
     */
    fun getNetConnectType(context: Context): String {
        val connManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return CONN_TYPE_NONE

        var info: NetworkInfo? = null
        info = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (null != info) {
            val wifiState = info.state
            if (NetworkInfo.State.CONNECTED == wifiState) {
                return CONN_TYPE_WIFI
            }
        } else {
        }

        info = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (null != info) {
            val mobileState = info.state
            if (NetworkInfo.State.CONNECTED == mobileState) {
                return CONN_TYPE_GPRS
            }
        } else {
        }
        return CONN_TYPE_NONE
    }

    /**
     * 获得Proxy地址

     * @param context 上下文
     * *
     * @return Proxy地址
     */
    fun getProxy(context: Context): String? {
        var proxy: String? = null
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val networkinfo = connectivityManager.activeNetworkInfo
            if (networkinfo != null && networkinfo.isAvailable) {
                val stringExtraInfo = networkinfo.extraInfo
                if (stringExtraInfo != null && ("cmwap" == stringExtraInfo || "uniwap" == stringExtraInfo)) {
                    proxy = "10.0.0.172:80"
                } else if (stringExtraInfo != null && "ctwap" == stringExtraInfo) {
                    proxy = "10.0.0.200:80"
                }
            }
        }

        return proxy
    }

    /*
     * 获取SIM卡状态
     */

    /**
     * 获取SIM卡状态

     * @param context 上下文
     * *
     * @return SIM卡状态
     */
    fun getSimState(context: Context): Int {
        val telMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simState = telMgr.simState
        if (simState == TelephonyManager.SIM_STATE_READY) {
            return SIM_OK
        } else if (simState == TelephonyManager.SIM_STATE_ABSENT) {
            return SIM_NO
        } else {
            return SIM_UNKNOW
        }
    }

    /*	获取运营商类型
     * 	此方法判断不是很全
     * */

    /**
     * 获取运营商类型,此方法判断不是很全

     * @param context 上下文
     * *
     * @param nsp     StringBuffer
     * *
     * @return 营商类型
     */
    fun getNSP(context: Context, nsp: StringBuffer?): Int {

        if (getSimState(context) == SIM_OK) {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val operator = tm.networkOperatorName

            //String numeric = tm.getNetworkOperator();

            if (operator == null || operator.length == 0) {
                return SIM_UNKNOW
            }
            if (nsp != null) {
                nsp.delete(0, nsp.length)
                nsp.append(operator)
            }
            if (operator.compareTo("中国移动", ignoreCase = true) == 0
                    || operator.compareTo("CMCC", ignoreCase = true) == 0
                    || operator.compareTo("China Mobile", ignoreCase = true) == 0
                    || operator.compareTo("46000", ignoreCase = true) == 0
                    || operator.compareTo("46002", ignoreCase = true) == 0) {
                return CHINA_MOBILE
            } else if (operator.compareTo("中国电信", ignoreCase = true) == 0
                    || operator.compareTo("China Telecom", ignoreCase = true) == 0
                    || operator.compareTo("46003", ignoreCase = true) == 0
                    || operator.compareTo("China Telcom", ignoreCase = true) == 0) {
                return CHINA_TELECOM
            } else if (operator.compareTo("中国联通", ignoreCase = true) == 0
                    || operator.compareTo("China Unicom", ignoreCase = true) == 0
                    || operator.compareTo("46001", ignoreCase = true) == 0
                    || operator.compareTo("CU-GSM", ignoreCase = true) == 0) {
                return CHINA_UNICOM
            } else {
                return SIM_UNKNOW
            }
        } else {
            return SIM_NO
        }
    }
}