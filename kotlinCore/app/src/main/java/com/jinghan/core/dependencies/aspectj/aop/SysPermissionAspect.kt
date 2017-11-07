package com.jinghan.core.dependencies.aspectj.aop

import com.jinghan.app.AppContext
import com.jinghan.core.dependencies.aspectj.annotation.Permission
import com.jinghan.core.helper.PermissionUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect


/**
 * @author liuzeren
 * @time 2017/11/6    上午10:42
 * @mail lzr319@163.com
 */
@Aspect
class SysPermissionAspect {

    @Around("execution(@com.jinghan.core.dependencies.aspectj.annotation.Permission * *(..)) && @annotation(permission)")
    @Throws(Throwable::class)
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint, permission: Permission) {
        val ac = AppContext.instance.currentActivity()

        PermissionUtils.requestPermissionsResult(ac, 1,permission.value, object : PermissionUtils.OnPermissionListener {
            override fun onPermissionGranted() {
                //权限申请成功，执行原方法
                joinPoint.proceed()
            }

            override fun onPermissionDenied() {
                //TODO 申请权限被拒
            }
        })
    }
}