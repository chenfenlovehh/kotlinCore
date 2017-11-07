package com.jinghan.core.dependencies.dragger2.scope

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import javax.inject.Scope

/**
 * Created by liuzeren on 2016/4/13.
 */
@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
annotation class ActivityScoped