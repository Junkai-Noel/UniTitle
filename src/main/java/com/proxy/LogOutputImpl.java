package com.proxy;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogOutputImpl implements LogOutput {

    private static final Logger log = LoggerFactory.getLogger(LogOutputImpl.class);

    @Override
    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void servicePointcut(){}
    @Override
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void controllerPointcut(){}

    @Around("servicePointcut()")
    public Object aroundService(@NotNull ProceedingJoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();
        Object result = null;
        try{
            log.info("Service层方法{}执行，参数列表{}",methodName,args);
            result = joinPoint.proceed(args);
            log.info("Service层返回值{}",result);
        }catch (Throwable e){
            log.error(e.getMessage());
        }
        return result;
    }

    @Around("controllerPointcut()")
    public Object aroundController(@NotNull ProceedingJoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();
        Object result = null;
        try{
            log.info("Controller层方法{}执行，参数列表{}",methodName,args);
            result = joinPoint.proceed(args);
            log.info("Controller层返回值{}",result);
        }catch (Throwable e){
            log.error(e.getMessage());
        }
        return result;
    }

}
