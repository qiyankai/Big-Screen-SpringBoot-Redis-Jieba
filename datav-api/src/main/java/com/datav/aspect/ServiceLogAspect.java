package com.datav.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLogAspect {

    public static final Logger log = LoggerFactory.getLogger(ServiceLogAspect.class);

    /**
     *  切面表达式
     *  execution   表示要执行的表达式主体
     *  第一处 *       代表方法返回类型    * 代表所有类型
     *  第二处 包名     代表aop监控的类所在的包
     *  第三处 ..      代表该包及其子包下的所有类方法
     *  第四处 *       代表类名， * 代表所有类
     *  第五处 *（..）  *代表类中的方法名， （..）表示方法中的任何参数
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.datav.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("========= 开始执行 {}.{} =========", joinPoint.getClass(), joinPoint.getSignature().getName());

        // 开始记录时间
        long begin = System.currentTimeMillis();
        // 执行目标service
        Object result = joinPoint.proceed();

        // 记录结束时间
        long end = System.currentTimeMillis();
        long takeTime = end - begin;

        if (takeTime > 3000) {
            log.error("===== 执行结束，耗时：{} 毫秒", takeTime);
        }else if (takeTime > 2000) {
            log.warn("===== 执行结束，耗时：{} 毫秒", takeTime);
        }else {
            log.info("===== 执行结束，耗时：{} 毫秒", takeTime);
        }
        return result;
    }


}
