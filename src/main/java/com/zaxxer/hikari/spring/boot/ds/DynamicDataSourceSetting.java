/*
 * Copyright (c) 2010-2020, vindell (https://github.com/vindell).
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

public class DynamicDataSourceSetting {

	/** driverClassName: 连接数据库的驱动名称，如果没有设置Hikari会自动根据数据库连接地址进行匹配 */
	private String driverClassName;
	/** name: 数据源名称；配置这个属性的意义在于，如果存在多个数据源，监控的时候可以通过名字来区分开来 */
	private String name;
	/** jdbcUrl: 连接数据库的url，不同数据库不一样 */
	private String url;
	/** username: 连接数据库的用户名 */
	private String username;
	/** password: 连接数据库的密码 */
	private String password;

	public DynamicDataSourceSetting(String name, String url, String username, String password) {
		this.name = name;
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public DynamicDataSourceSetting(String driverClassName, String name, String url, String username, String password) {
		this.driverClassName = driverClassName;
		this.name = name;
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
