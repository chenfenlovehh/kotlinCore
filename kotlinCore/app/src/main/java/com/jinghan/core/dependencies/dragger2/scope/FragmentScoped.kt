package com.jinghan.core.dependencies.dragger2.scope

import javax.inject.Scope

/**
 * @author liuzeren
 * @time 2017/11/2    下午3:06
 * @mail lzr319@163.com
 */
@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@kotlin.annotation.Target(AnnotationTarget.TYPE,AnnotationTarget.FUNCTION)
annotation class FragmentScoped