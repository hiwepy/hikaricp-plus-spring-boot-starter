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
package com.zaxxer.hikari.spring.boot.ds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicDataSourceContextHolder {
	
	public static final String MASTER_DATASOURCE = "defaultDataSource";
	private final static Logger logger = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);
	
	/**
     * A provider of random values.
     */
    private final static Random random = new Random();
	/**
     * 用于在切换数据源时保证不会被其他线程修改
     */
    private static Lock lock = new ReentrantLock();
    /**
     * Maintain variable for every thread, to avoid effect other thread
     */
	private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<String>() {
		
		@Override
		protected String initialValue() {
			return MASTER_DATASOURCE;
		}
		
	};
    
    /**
     * All DataSource List
     */
    public static List<Object> dataSourceKeys = new ArrayList<Object>();

    /**
     * To switch DataSource
     * @param key the key
     */
    public static void setDataSourceKey(String key) {
        CONTEXT_HOLDER.set(key);
    }

    /**
	 * Use master data source.
	 */
	private static void useMasterDataSource() {
		CONTEXT_HOLDER.set(MASTER_DATASOURCE);
	}

    /**
     * 当使用只读数据源时通过轮循方式选择要使用的数据源
     */
    public static void useSlaveDataSource() {
    	
        lock.lock();
        
        try {
        	int datasourceKeyIndex = random.nextInt(dataSourceKeys.size());
            CONTEXT_HOLDER.set(String.valueOf(dataSourceKeys.get(datasourceKeyIndex)));
        } catch (Exception e) {
            logger.error("Switch slave datasource failed, error message is {}", e.getMessage());
            useMasterDataSource();
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

	/**
     * Get current DataSource
     * @return data source key
     */
    public static String getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * To set DataSource as default
     */
    public static void clearDataSourceKey() {
        CONTEXT_HOLDER.remove();
    }

    /**
     * Check if give DataSource is in current DataSource list
     *
     * @param key the key
     * @return boolean boolean
     */
    public static boolean containDataSourceKey(String key) {
        return dataSourceKeys.contains(key);
    }
    
}
