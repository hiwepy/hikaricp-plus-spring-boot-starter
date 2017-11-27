package com.zaxxer.hikari.spring.boot.ds;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 数据库 - 驱动和连接示例
 */
public enum JDBCDriverEnum {
	
	/**
	 * DB2数据库驱动和链接
	 */
	DB2("DB2","DB2 数据库","com.ibm.db2.jcc.DB2Driver", "jdbc:db2://{ip}:{port}/{database}"),
	/**
	 * 内嵌模式Derby数据库驱动和链接
	 */
	DERBY("Embedded-Derby","内嵌模式Derby 数据库","org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:DerByDB;create=true"),
	/**
	 * 网络模式Derby数据库驱动和链接
	 */
	DERBY_CLIENT("Network-Server-Derby", "网络模式Derby 数据库","org.apache.derby.jdbc.ClientDriver", "jdbc:derby://{ip}:{port}/{database};create=true"),
	/**
	 * Hsqldb 数据库驱动和链接
	 */
	HSQLDB("Hsqldb","Hsqldb 数据库","org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:sample"), 
	/**
	 * Mariadb 数据库驱动和链接
	 */
	MARIADB("Mariadb","Mariadb 数据库","org.mariadb.jdbc.Driver", "jdbc:mariadb://localhost:3306/sample"), 
	/**
	 * MySQL数据库驱动和链接
	 */
	MYSQL("MySQL","MySQL 数据库","com.mysql.cj.jdbc.Driver", "jdbc:mysql://{ip}:{port}/{database}"), 
	/**
	 * Oracle数据库驱动和链接
	 */
	ORACLE("Oracle","Oracle 10g、11g 数据库","oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin:@{ip}:{port}:{database}"), 
	ORACLE12C("Oracle12c","Oracle12c 数据库","oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin:@{ip}:{port}/{database}"),
	/**
	 * PostgreSQL 数据库驱动和链接
	 */
	POSTGRESQL("PostgreSQL","PostgreSQL 数据库","org.postgresql.Driver", "jdbc:postgresql://{ip}:{port}/{database}"), 
	/**
	 * SqlServer2000 数据库驱动和链接
	 */
	SQLSERVER_2000("SqlServer2000","SqlServer2000 数据库","com.microsoft.jdbc.sqlserver.SQLServerDriver","jdbc:microsoft:sqlserver://{ip}:{port};DatabaseName={database}"),
	/**
	 * SqlServer2005 数据库驱动和链接
	 */
	SQLSERVER_2005("SqlServer2005","SqlServer2005 数据库","com.microsoft.sqlserver.jdbc.SQLServerDriver","jdbc:sqlserver://{ip}:{port};DatabaseName={database}");

	
	private String key;
	private String desc;
	private String className;
	private String url;

	private JDBCDriverEnum(String key, String desc, String className, String url) {
		this.key = key;
		this.desc = desc;
		this.className = className;
		this.url = url;
	}

	public String getKey() {
		return key;
	}

	public String getDesc() {
		return desc;
	}

	public String getDriverClass() {
		return className;
	}
	
	public String getDriverURL(String ip,String port,String dbname) {
		return url.replace("{ip}", ip).replace("{port}", port).replace("{database}", dbname);
	}
	
	public  Map<String, String> toMap() {
		Map<String, String> driverMap = new HashMap<String, String>();
		driverMap.put("key", this.getKey());
		driverMap.put("desc", this.getDesc());
		driverMap.put("className", this.getDriverClass());
		driverMap.put("url", url);
		return driverMap;
	}
	
	public static JDBCDriverEnum driver(String dbtype) {
		for (JDBCDriverEnum driverEnum : JDBCDriverEnum.values()) {
			if(driverEnum.getKey().equals(dbtype)) {
				return driverEnum;
			}
		}
		return null;
	}
	
	public static List<Map<String, String>> driverList() {
		List<Map<String, String>> driverList = new LinkedList<Map<String, String>>();
		for (JDBCDriverEnum driverEnum : JDBCDriverEnum.values()) {
			driverList.add(driverEnum.toMap());
		}
		return driverList;
	}
	
	/**
	 * 驱动是否存在
	 */
	public boolean hasDriver() {
		try {
			Class.forName(getDriverClass());
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
