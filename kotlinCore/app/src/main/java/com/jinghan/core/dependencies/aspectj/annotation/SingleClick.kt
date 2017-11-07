package com.jinghan.core.dependencies.aspectj.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @author liuzeren
 * @time 2017/11/6    上午10:23
 * @mail lzr319@163.com
 */
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class SingleClick