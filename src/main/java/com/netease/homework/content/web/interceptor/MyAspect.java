package com.netease.homework.content.web.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/2/8
 */
@Aspect
public class MyAspect {

//    @Around("execution(* com.netease.homework.content.mapper.ContentMapper.*(..))")
//    @Around("execution(@(@(com.netease.snailreader.common.component.intercept.Interceptable) *) * *(..))")
    @Around("@annotation(com.netease.snailreader.common.component.intercept.merge.annotation.MergeMap)")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint){
        System.out.println("-------around");
        try {
            Object obj = proceedingJoinPoint.proceed();
            return obj;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

}
