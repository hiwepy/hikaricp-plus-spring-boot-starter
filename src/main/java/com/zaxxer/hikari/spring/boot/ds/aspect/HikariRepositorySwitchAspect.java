package com.zaxxer.hikari.spring.boot.ds.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.spring.boot.ds.DataSourceContextHolder;
import com.zaxxer.hikari.spring.boot.ds.annotation.SwitchRepository;

/**
 * 
 * @className	： HikariRepositorySwitchAspect
 * @description	：数据源自动切换切面
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年11月27日 下午10:07:34
 * @version 	V1.0
 */
@Aspect
@Component
public class HikariRepositorySwitchAspect {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	//环绕通知   
	@Around("@annotation(com.zaxxer.hikari.spring.boot.ds.annotation.SwitchRepository) and @annotation(repository)")
	public Object around(ProceedingJoinPoint joinPoint, SwitchRepository repository) throws Throwable {
		String oldRepository = DataSourceContextHolder.getDatabaseName();
    	try {
    		DataSourceContextHolder.setDatabaseName(repository.value());
    		return joinPoint.proceed();
        } finally {
        	if (logger.isDebugEnabled()) {
        		logger.debug("invoke(ProceedingJoinPoint) - end"); //$NON-NLS-1$
            }
    		DataSourceContextHolder.setDatabaseName(oldRepository);
        }
    }  
	
}
