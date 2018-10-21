/*
 * Copyright (c) 2017, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.zaxxer.hikari.spring.boot.ds.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.biz.jdbc.DataSourceRoutingKeyHolder;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.spring.boot.ds.annotation.SwitchRepository;

/**
 * 数据源自动切换切面
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
@Aspect
@Component
public class DynamicDataSourceSwitchAspect {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	//环绕通知   
	@Around("@annotation(com.zaxxer.hikari.spring.boot.ds.annotation.SwitchRepository) and @annotation(repository)")
	public Object around(ProceedingJoinPoint joinPoint, SwitchRepository repository) throws Throwable {
		String oldRepository = DataSourceRoutingKeyHolder.getDataSourceKey();
    	try {
    		DataSourceRoutingKeyHolder.setDataSourceKey(repository.value());
    		return joinPoint.proceed();
        } finally {
        	if (logger.isDebugEnabled()) {
        		logger.debug("invoke(ProceedingJoinPoint) - end"); //$NON-NLS-1$
            }
    		DataSourceRoutingKeyHolder.setDataSourceKey(oldRepository);
        }
    }  
	
}
