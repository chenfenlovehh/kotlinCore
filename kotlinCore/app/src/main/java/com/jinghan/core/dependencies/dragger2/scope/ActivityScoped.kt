package com.jinghan.core.dependencies.dragger2.scope

import javax.inject.Scope

/**
 * Created by liuzeren on 2016/4/13.
 */
@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@kotlin.annotation.Target(AnnotationTarget.TYPE,AnnotationTarget.FUNCTION)
annotation class ActivityScoped