package com.jinghan.core.dependencies.aspectj.aop

import android.view.View
import com.jinghan.core.R
import com.orhanobut.logger.Logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import java.util.*

/**
 * @author liuzeren
 * @time 2017/11/6    上午10:27
 * @mail lzr319@163.com
 */
@Aspect
class SingleClickAspect{

    @Pointcut("execution(@com.jinghan.core.dependencies.aspectj.annotation.SingleClick * *(..))") //方法切入点
    fun methodAnnotated(){}

    @Around("methodAnnotated()")//在连接点进行方法替换
    fun aroundJoinPoint(joinPoint:ProceedingJoinPoint){
        var view : View? = null

        for(arg in joinPoint.args)
            if(arg is View) {
                view = arg
                break
            }

        var tag = view?.getTag()
        val lastClickTime = if (tag != null) tag as Long else 0

        Logger.i("SingleClickAspect", "lastClickTime:" + lastClickTime)
        val currentTime = Calendar.getInstance().timeInMillis
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {//过滤掉600毫秒内的连续点击
            view?.setTag(TIME_TAG, currentTime)
            Logger.i("SingleClickAspect", "currentTime:" + currentTime)
            joinPoint.proceed()//执行原方法
        }
    }

    companion object {
        internal var TIME_TAG = R.id.aspectj_click_time
        val MIN_CLICK_DELAY_TIME = 6000
    }
}