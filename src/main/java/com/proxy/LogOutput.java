package com.proxy;

import org.aspectj.lang.ProceedingJoinPoint;

public interface LogOutput {
     void servicePointcut();
     void controllerPointcut();
     Object aroundService( ProceedingJoinPoint joinPoint);
     Object aroundController( ProceedingJoinPoint joinPoint);
}
