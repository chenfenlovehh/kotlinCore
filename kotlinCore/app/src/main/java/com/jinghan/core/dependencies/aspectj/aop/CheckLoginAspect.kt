package com.jinghan.core.dependencies.aspectj.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

/**
 * @author liuzeren
 * @time 2017/11/6    上午10:41
 * @mail lzr319@163.com
 */
@Aspect
class CheckLoginAspect {

    @Pointcut("execution(@com.jinghan.core.dependencies.aspectj.annotation.CheckLogin * *(..))")
    fun methodAnnotated() {
    }

    @Around("methodAnnotated()")
    @Throws(Throwable::class)
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint) {
        /*if (null == SpUtil.getUser()) {
            Snackbar.make(App.getAppContext().getCurActivity().getWindow().getDecorView(), "请先登录!", Snackbar.LENGTH_LONG)
                    .setAction("登录", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TRouter.go(C.LOGIN);
                        }
                    }).show();
            return;
        }
        joinPoint.proceed();//执行原方法*/
    }
}
