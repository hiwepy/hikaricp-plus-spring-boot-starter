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

public class DataSourceContextHolder {

	public static final String DEFAULT_DATASOURCE = "defaultDataSource";
	
	private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<String>() {
		
		@Override
		protected String initialValue() {
			return DEFAULT_DATASOURCE;
		}
		
	};

	public static void setDatabaseName(String name) {
		CONTEXT_HOLDER.set(name);
	}

	public static String getDatabaseName() {
		return CONTEXT_HOLDER.get();
	}
	
}
