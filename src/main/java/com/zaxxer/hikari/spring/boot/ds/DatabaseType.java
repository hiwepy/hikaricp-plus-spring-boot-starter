/*
 * Copyright (c) 2017, hiwepy (https://github.com/hiwepy).
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

/**
 * 关系型数据库 - 驱动和连接示例
 */
public enum DatabaseType {

	/**
	 * Microsoft Azure Cloud #
	 * jdbc:sqlserver://[host-name]:[port];databaseName=[database-name]
	 */
	AZURE("azure", "Microsoft Azure Cloud", "com.microsoft.sqlserver.jdbc.SQLServerDriver",
			"jdbc:sqlserver://[host-name]:[port];databaseName=[database-name]",
			"jdbc:sqlserver://%s:%d;databaseName=%s", 1433, true),
	/**
	 * DB2 # jdbc:db2://[host-name]:[port]/[database-name]
	 */
	DB2("db2", "IBM DB2", "com.ibm.db2.jcc.DB2Driver", 
			"jdbc:db2://[host-name]:[port]/[database-name]", "jdbc:db2://%s:%d/%s", 50000, true),
	/**
	 * Derby Embedded # jdbc:derby:[database-name];create=true
	 */
	DERBY_EMBEDDED("derby-embedded", "Derby Embedded", "org.apache.derby.jdbc.EmbeddedDriver",
			"jdbc:derby:[database-name];create=true", "jdbc:derby:%s;create=true", 1527, false),
	/**
	 * Derby Network # jdbc:derby://[host-name]:[port]/[database-name]
	 */
	DERBY_REMOTE("derby-network", "Derby Network", "org.apache.derby.jdbc.ClientDriver",
			"jdbc:derby://[host-name]:[port]/[database-name]", "jdbc:derby://%s:%d/%s", 1527, true),

	/*
	 * HyperSQL ： http://hsqldb.org/doc/2.0/guide/index.html
	 * http://hsqldb.org/doc/2.0/guide/running-chapt.html#rgc_connecting_db
	 */

	/**
	 * HyperSQL HSQL Server # jdbc:hsqldb:hsql://[host-name]/[database-name]
	 */
	HSQLDB_HSQL("hsqldb-hsql", "HyperSQL HSQL Server", "org.hsqldb.jdbc.JDBCDriver",
			"jdbc:hsqldb:hsql://[host-name]/[database-name]", "jdbc:hsqldb:hsql://%s/%s", 9001, true),
	/**
	 * HyperSQL HSQL Server（SSL） # jdbc:hsqldb:hsqls://[host-name]/[database-name]
	 */
	HSQLDB_HSQLS("hsqldb-hsqls", "HyperSQL HSQL Server（SSL）", "org.hsqldb.jdbc.JDBCDriver",
			"jdbc:hsqldb:hsqls://[host-name]/[database-name]", "jdbc:hsqldb:hsqls://%s/%s", 554, true),
	/**
	 * HyperSQL HTTP Server # jdbc:hsqldb:http://[host-name]:[port]/[database-name]
	 */
	HSQLDB_HTTP("hsqldb-http", "HyperSQL HTTP Server", "org.hsqldb.jdbc.JDBCDriver",
			"jdbc:hsqldb:http://[host-name]:[port]/[database-name]", "jdbc:hsqldb:http://%s:%d/%s", 80, true),
	/**
	 * HyperSQL HTTP Server（SSL） #
	 * jdbc:hsqldb:https://[host-name]:[port]/[database-name]
	 */
	HSQLDB_HTTPS("hsqldb-https", "HyperSQL HTTP Server（SSL）", "org.hsqldb.jdbc.JDBCDriver",
			"jdbc:hsqldb:https://[host-name]:[port]/[database-name]", "jdbc:hsqldb:https://%s:%d/%s", 443, true),
	/**
	 * HyperSQL BER # jdbc:hsqldb:file:[file-path]/[database-name];ifexists=true
	 */
	HSQLDB_BER("hsqldb-file", "HyperSQL BER", "org.hsqldb.jdbc.JDBCDriver",
			"jdbc:hsqldb:file:[file-path]/[database-name];ifexists=true", "jdbc:hsqldb:file:%s/%s;ifexists=true", 
			9101, false),
	/**
	 * Apache Hive # jdbc:hive2://[host-name]:[port]/[database-name]
	 */
	HIVE("hive", "Apache Hive", "org.apache.hive.jdbc.HiveDriver", 
			"jdbc:hive2://[host-name]:[port]/[database-name]", "jdbc:hive2://%s:%d/%s", 10000, true),
	/**
	 * Mariadb # jdbc:mariadb://[host-name]:[port]/[database-name]
	 */
	MARIADB("mariadb", "Mariadb", "org.mariadb.jdbc.Driver", 
			"jdbc:mariadb://[host-name]:[port]/[database-name]", "jdbc:mariadb://%s:%d/%s", 3306, true),
	/**
	 * Microsoft SQL Server 2000 #
	 * jdbc:microsoft:sqlserver://[host-name]:[port];DatabaseName=[database-name]
	 * https://docs.microsoft.com/zh-cn/sql/connect/jdbc/microsoft-jdbc-driver-for-sql-server-support-matrix?view=sql-server-2017
	 * 
	 */
	MSSQL_2000("sqlserver2000", "Microsoft SQL Server 2000", "com.microsoft.sqlserver.jdbc.SQLServerDriver",
			"jdbc:microsoft:sqlserver://[host-name]:[port];DatabaseName=[database-name]",
			"jdbc:microsoft:sqlserver://%s:%d;DatabaseName=%s;integratedSecurity=false;", 1433, true),
	/**
	 * Microsoft SQL Server 2005 + #
	 * jdbc:sqlserver://[host-name]:[port];DatabaseName=[database-name]
	 */
	MSSQL("sqlserver", "Microsoft SQL Server 2005及以上版本", "com.microsoft.sqlserver.jdbc.SQLServerDriver",
			"jdbc:sqlserver://[host-name]:[port];DatabaseName=[database-name]",
			"jdbc:sqlserver://%s:%d;DatabaseName=%s;integratedSecurity=false;", 1433, true),
	/**
	 * MySQL #
	 * jdbc:mysql://[host-name]:[port]/[database-name]?rewriteBatchedStatements=true&amp;useUnicode=true&amp;characterEncoding=UTF-8
	 */
	MYSQL("mysql", "MySQL", "com.mysql.cj.jdbc.Driver",
			"jdbc:mysql://[host-name]:[port]/[database-name]?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=UTF-8",
			"jdbc:mysql://%s:%d/%s?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=UTF-8", 3306, true),
	/**
	 * Oracle 10g、11g # jdbc:oracle:thin:@[host-name]:[port]:[database-name]
	 */
	ORACLE("oracle", "Oracle 10g、11g", "oracle.jdbc.OracleDriver",
			"jdbc:oracle:thin:@[host-name]:[port]:[database-name]", "jdbc:oracle:thin:@%s:%d:%s", 1521, true),
	/**
	 * Oracle 12c # jdbc:oracle:thin:@//[host-name]:[port]/[database-name]
	 */
	ORACLE_12C("oracle-12c", "Oracle 12c", "oracle.jdbc.OracleDriver",
			"jdbc:oracle:thin:@//[host-name]:[port]/[database-name]", "jdbc:oracle:thin:@//%s:%d/%s", 1521, true),
	/**
	 * Oracle 18c # jdbc:oracle:thin:@//[host-name]:[port]/[database-name]
	 */
	ORACLE_18C("oracle-18c", "Oracle 12c", "oracle.jdbc.OracleDriver",
			"jdbc:oracle:thin:@//[host-name]:[port]/[database-name]", "jdbc:oracle:thin:@//%s:%d/%s", 1521, true),
	
	/**
	 * PostgreSQL # jdbc:postgresql://[host-name]:[port]/[database-name]
	 */
	POSTGRESQL("postgreSQL", "PostgreSQL", "org.postgresql.Driver",
			"jdbc:postgresql://[host-name]:[port]/[database-name]", "jdbc:postgresql://%s:%d/%s", 5432, true),
	/**
	 * Amazon Redshift ： jdbc:redshift://[host-name]:[port]/[database-name]
	 */
	REDSHIFT("redshift", "Amazon Redshift", "com.amazon.redshift.jdbc41.Driver",
			"jdbc:redshift://[host-name]:[port]/[database-name]", "jdbc:redshift://%s:%d/%s", 5439, true),
	/**
	 * Teradata # jdbc:teradata://[host-name]/DBS_PORT=[port],DATABASE=[database-name]
	 */
	TERADATA("teradata", "Teradata", "com.teradata.jdbc.TeraDriver",
			"jdbc:teradata://[host-name]/DBS_PORT=[port],DATABASE=[database-name]",
			"jdbc:teradata://%s/DBS_PORT=%d,DATABASE=%s", 8002, true),
	/**
	 * IBM Netezza # jdbc:netezza://[host-name]:[port]:[database-name]
	 */
	NETEZZA("netezza", "IBM Netezza", "org.netezza.Driver", 
			"jdbc:netezza://[host-name]:[port]:[database-name]", "jdbc:netezza://%s:%d:%s", 5480, true),
	/**
	 * HPE Vertica # jdbc:vertica://[host-name]:[port]/[database-name]
	 */
	VERTICA("vertica", "HPE Vertica", "com.vertica.jdbc.Driver", 
			"jdbc:vertica://[host-name]:[port]/[database-name]", "jdbc:vertica://%s:%d/%s", 5433, true);

	private String key;
	private String vendor;
	private String driver;
	private String placeholder;
	private String url;
	private int port;
	private boolean standlone;

	private DatabaseType(String key, String vendor, String driver, String placeholder, String url, int port,
			boolean standlone) {
		this.key = key;
		this.vendor = vendor;
		this.driver = driver;
		this.placeholder = placeholder;
		this.url = url;
		this.port = port;
		this.standlone = standlone;
	}

	public String getKey() {
		return key;
	}

	public String getVendor() {
		return vendor;
	}

	public int getDefaultPort() {
		return port;
	}

	public String getDriverClass() {
		return driver;
	}

	public boolean isStandlone() {
		return standlone;
	}
	
	public String getPlaceholder() {
		return placeholder;
	}

	public boolean equals(DatabaseType dbtype) {
		return this.compareTo(dbtype) == 0;
	}

	public boolean equals(String dbtype) {
		return this.compareTo(DatabaseType.valueOfIgnoreCase(dbtype)) == 0;
	}

	public static DatabaseType valueOfIgnoreCase(String dbtype) {
		for (DatabaseType dbtypeEnum : DatabaseType.values()) {
			if (dbtypeEnum.getKey().equals(dbtype)) {
				return dbtypeEnum;
			}
		}
		throw new NoSuchElementException("Cannot found DatabaseType with dbtype '" + dbtype + "'.");
	}

	public String getDriverURL(String ip, int port, String dbname) {
		return String.format(url, ip, port, dbname);
	}

	public String getDriverURL(String ip, int port, String dbname, Map<String,String> properties, String separator) {
		if(properties != null && !properties.isEmpty()) {
			Iterator<Entry<String, String>> ite = properties.entrySet().iterator();
			List<String> args = new ArrayList<>();
			while (ite.hasNext()) {
				Entry<String, String> entry = ite.next();
				args.add(entry.getKey() + "=" + entry.getValue());
			}
			return String.format(url, ip, port, dbname) + separator + StringUtils.join(args, separator);
		}
		return String.format(url, ip, port, dbname);
	}
	
	public Map<String, String> toMap() {
		Map<String, String> driverMap = new HashMap<String, String>();
		driverMap.put("key", this.getKey());
		driverMap.put("vendor", this.getVendor());
		driverMap.put("driver", this.getDriverClass());
		driverMap.put("port", String.valueOf(this.getDefaultPort()));
		driverMap.put("placeholder", this.getPlaceholder());
		return driverMap;
	}

	public static List<Map<String, String>> driverList() {
		return driverList(false);
	}
	
	public static List<Map<String, String>> driverList(boolean standloneOnly) {
		List<Map<String, String>> driverList = new LinkedList<Map<String, String>>();
		for (DatabaseType dbType : DatabaseType.values()) {
			if(standloneOnly) {
				if (dbType.isStandlone()) {
					driverList.add(dbType.toMap());
				}
			} else {
				driverList.add(dbType.toMap());
			}
		}
		return driverList;
	}

	public boolean hasDriver() {
		try {
			Class.forName(getDriverClass());
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	 
}