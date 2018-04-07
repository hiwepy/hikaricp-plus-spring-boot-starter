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

import java.sql.DriverManager;

public class JDBCUtils {

	/**
	 * 
	 * @description	： TODO
	 * @author 		： <a href="https://github.com/vindell">vindell</a>
	 * @date 		：2017年11月27日 下午8:49:08
	 * @param className
	 * @param URL
	 * @param Username
	 * @param Password
	 * @return
	 */
	public static boolean testConnection(String className,String URL,String Username,String Password) {
		try {
			Class.forName(className).newInstance();
			DriverManager.getConnection(URL, Username, Password);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	public static String getDriverClass(String dbtype) {
		JDBCDriverEnum driverEnum = JDBCDriverEnum.driver(dbtype);
		return driverEnum != null ? driverEnum.getDriverClass() : null; 
	}
	
}
