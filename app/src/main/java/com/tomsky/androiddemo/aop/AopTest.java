package com.tomsky.androiddemo.aop;

import android.util.Log;

//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by wangzhitao on 2019/12/05
 **/
/**
 * 添加切面注解
 */
//@Aspect
public class AopTest {
    private static final String TAG = "wzt-aop";
    /**
     * 定义切入点（定义那些类或者方法需要改变）
     */
//    @Pointcut("execution(*  com.tomsky.androiddemo.activity.DynamicUIActivity..*ck(..))")
    public void pointcut1() {
    }
    /**
     * 使用注解方式，定义注解
     */
//    @Pointcut("execution(@com.tomsky.androiddemo.aop.DebugLog * *ck(..))")
    public void pointcut() {
    }
    /**
     * 前置通知，切点之前执行
     * @param point
     */
//    @Before("pointcut()")
//    public void logBefore(JoinPoint point){
//        Log.e(TAG,"logBefore:"+point.toLongString());
//    }
    /**
     * 环绕通知，切点前后执行
     * @param joinPoint
     * @throws Throwable
     */
//    @Around("pointcut()")
//    public void logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        Log.e(TAG,"logAround:"+joinPoint.toLongString());
//        // 1.执行切点函数（如果不调用该方法，切点函数不被执行）
//        joinPoint.proceed();
//    }
    /**
     * 后置通知，切点之后执行
     * @throws Throwable
     */
//    @After("pointcut()")
//    public void logAfter(JoinPoint point){
//        Log.e(TAG,"logAfter");
//    }
    /**
     * 返回通知，切点方法返回结果之后执行
     * @throws Throwable
     */
//    @AfterReturning(value="execution(*  com.tomsky.androiddemo.activity.DynamicUIActivity..*ck(..))", returning="returnValue")
//    public void logAfterReturning(JoinPoint point, Object returnValue){
//        Log.e(TAG,"logAfterReturning, returnValue:"+returnValue);
//    }
    /**
     * 异常通知，切点抛出异常时执行
     * @throws Throwable
     */
//    @AfterThrowing(value = "pointcut()",throwing = "ex")
//    public void logAfterThrowing(Throwable ex){
//        Log.e(TAG,"logAfterThrowing : "+ex.getMessage());
//    }
}
