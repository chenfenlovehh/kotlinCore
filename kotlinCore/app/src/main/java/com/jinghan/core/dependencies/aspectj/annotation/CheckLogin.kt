package com.jinghan.core.dependencies.aspectj.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * 检测登录，即必须要先登录才能使用的功能
 * @author liuzeren
 * @time 2017/11/6    上午10:26
 * @mail lzr319@163.com
 */

@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class CheckLogin