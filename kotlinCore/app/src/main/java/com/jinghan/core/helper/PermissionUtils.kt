package com.jinghan.core.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.annotation.TargetApi
import android.support.v4.content.ContextCompat
import java.util.*

/**
 * @author liuzeren
 * @time 2017/11/3    下午2:54
 * @mail lzr319@163.com
 */
object PermissionUtils{

    private var mRequestCode = -1

    interface OnPermissionListener {
        fun onPermissionGranted()

        fun onPermissionDenied()
    }

    private var mOnPermissionListener: OnPermissionListener? = null

    /** SD卡权限  */
    val PERMISSION_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    /** 相机权限  */
    val PERMISSION_CAMERA = arrayOf(Manifest.permission.CAMERA)

    /** 录音权限  */
    val PERMISSION_AUDIO = arrayOf(Manifest.permission.RECORD_AUDIO)

    /** 日历权限  */
    val CALENDAR = arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)

    /**
     * 检查权限

     * @param ctx
     * *
     * @param permissions
     * *
     * @return
     */
    fun checkPermissions(ctx: Context, permissions: Array<out String>): Boolean {

        if (permissions.size < 1) {
            return true
        }

        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(ctx, permission) != PackageManager.PERMISSION_GRANTED) {

                return false
            }
        }
        return true
    }

    /**
     * 检查权限

     * @param ctx
     * *
     * @param permission
     * *
     * @return
     */
    fun checkPermission(ctx: Context, permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(ctx, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检查SD卡权限
     * @param ctx
     * *
     * @return
     */
    fun checkStoragePermissions(ctx: Context): Boolean {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//6.0以下不需要动态申请权限
            return true
        }

        return checkPermissions(ctx, PERMISSION_STORAGE)
    }

    fun requestPermissionsResult(activity: Activity, requestCode: Int, permission: Array<out String>, callback: OnPermissionListener) {
        requestPermissions(activity, requestCode, permission, callback)
    }

    fun requestPermissionsResult(fragment: android.app.Fragment, requestCode: Int, permission: Array<out String>, callback: OnPermissionListener) {
        requestPermissions(fragment, requestCode, permission, callback)
    }

    fun requestPermissionsResult(fragment: android.support.v4.app.Fragment, requestCode: Int, permission: Array<out String>, callback: OnPermissionListener) {
        requestPermissions(fragment, requestCode, permission, callback)
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun requestPermissions(obj: Any, requestCode: Int, permissions: Array<out String>, callback: OnPermissionListener) {

        checkCallingObjectSuitability(obj)
        mOnPermissionListener = callback

        if (checkPermissions(getContext(obj), permissions)) {
            mOnPermissionListener?.onPermissionGranted()
        } else {
            val deniedPermissions = getDeniedPermissions(getContext(obj), permissions)
            if (deniedPermissions.size > 0) {
                mRequestCode = requestCode
                if (obj is Activity) {
                    obj.requestPermissions(deniedPermissions
                            .toTypedArray(), requestCode)
                } else if (obj is android.app.Fragment) {
                    obj.requestPermissions(deniedPermissions
                            .toTypedArray(), requestCode)
                } else if (obj is android.support.v4.app.Fragment) {
                    obj.requestPermissions(deniedPermissions
                            .toTypedArray(), requestCode)
                } else {
                    mRequestCode = -1
                }
            }
        }
    }

    /**
     * 检查所传递对象的正确性
     *
     * @param object 必须为 activity or fragment
     */
    private fun checkCallingObjectSuitability(obj: Any?) {
        if (obj == null) {
            throw NullPointerException("Activity or Fragment should not be null")
        }

        val isActivity = obj is android.app.Activity
        val isSupportFragment = obj is android.support.v4.app.Fragment
        val isAppFragment = obj is android.app.Fragment

        if (!(isActivity || isSupportFragment || isAppFragment)) {
            throw IllegalArgumentException(
                    "Caller must be an Activity or a Fragment")
        }
    }

    /**
     * 获取上下文
     */
    private fun getContext(obj: Any): Context {
        val context: Context
        if (obj is android.app.Fragment) {
            context = obj.activity
        } else if (obj is android.support.v4.app.Fragment) {
            context = obj.activity
        } else {
            context = obj as Activity
        }
        return context
    }

    /**
     * 获取权限列表中所有需要授权的权限
     *
     * @param context     上下文
     * @param permissions 权限列表
     * @return
     */
    private fun getDeniedPermissions(context: Context, permissions: Array<out String>): List<String> {
        val deniedPermissions = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission)
            }
        }
        return deniedPermissions
    }

    /**
     * 请求权限结果，对应onRequestPermissionsResult()方法。
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (mRequestCode !== -1 && requestCode == mRequestCode) {
            if (verifyPermissions(grantResults)) {
                mOnPermissionListener?.onPermissionGranted()
            } else {
                mOnPermissionListener?.onPermissionDenied()
            }
        }
    }

    /**
     * 验证权限是否都已经授权
     */
    private fun verifyPermissions(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}