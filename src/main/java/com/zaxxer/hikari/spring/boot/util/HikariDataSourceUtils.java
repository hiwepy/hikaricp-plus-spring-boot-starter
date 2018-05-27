package com.zaxxer.hikari.spring.boot.util;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.util.CollectionUtils;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.spring.boot.HikaricpProperties;

public class HikariDataSourceUtils {

	public static <T extends DataSource> HikariDataSource createDataSource(DataSourceProperties properties, HikaricpProperties hikariProperties, 
			String jdbcUrl, String username, String password) {
		
		DataSourceProperties tmProperties = new DataSourceProperties();
		
		tmProperties.setName(properties.getName());
		tmProperties.setType(HikariDataSource.class);
		// driverClassName : 数据库驱动 
		tmProperties.setDriverClassName(properties.determineDriverClassName());
		// jdbcUrl: 连接数据库的url
		tmProperties.setUrl(jdbcUrl);
		// username: 连接数据库的用户名
		tmProperties.setUsername(username);
		// password: 连接数据库的密码
		tmProperties.setPassword(password);
		
		// 创建 HikariDataSource 数据源对象
		HikariDataSource dataSource = createDataSource(tmProperties, tmProperties.getType());

		// Hikari 连接池参数
		
		// initializationFailTimeout: 连接池初始化失败超时时间；单位 (毫秒) 
		dataSource.setInitializationFailTimeout(hikariProperties.getInitializationFailTimeout());
		// minIdle: 连接池最小连接数量 
		dataSource.setMinimumIdle(hikariProperties.getMinIdle());
		// maxPoolSize: 连接池最大连接数量 
		dataSource.setMaximumPoolSize(hikariProperties.getMaxPoolSize());
		// maxLifetime: 连接存活时间 
		dataSource.setMaxLifetime(hikariProperties.getMaxLifetime());

		if (StringUtils.isNotEmpty(hikariProperties.getConnectionInitSql())) {
			// connectionInitSql: 连接初始化SQL语句,在连接加入连接池前执行 
			dataSource.setConnectionInitSql(hikariProperties.getConnectionInitSql());
			
		}
		if (StringUtils.isNotEmpty(hikariProperties.getConnectionTestQuery())) {
			// connectionTestQuery: 连接有效性检查SQL语句,当执行连接检查时执行 
			dataSource.setConnectionTestQuery(hikariProperties.getConnectionTestQuery());
			// validationTimeout: 连接检查超时时间；单位 (毫秒) 
			dataSource.setValidationTimeout(hikariProperties.getValidationTimeout());
		}
		
		// connectionTimeout: 连接初始化超时时间；单位 (毫秒) 
		dataSource.setConnectionTimeout(hikariProperties.getConnectionTimeout());
		// idleTimeout: 连接空闲超时时间，当一个连接超出该时间，会被释放；单位 (毫秒) 
		dataSource.setIdleTimeout(hikariProperties.getIdleTimeout());
		// transactionIsolationName: 事务隔离名称 
		dataSource.setTransactionIsolation(hikariProperties.getTransactionIsolationName());
		// autoCommit: 是否自动提交事务 
		dataSource.setAutoCommit(hikariProperties.isAutoCommit());
		// readOnly: 是否只读 
		dataSource.setReadOnly(hikariProperties.isReadOnly());
		// isolateInternalQueries: 是否内部查询事务隔离 
		dataSource.setIsolateInternalQueries(hikariProperties.isIsolateInternalQueries());
		// registerMbeans: 是否注册JMX监控参数输出 
		dataSource.setRegisterMbeans(hikariProperties.isRegisterMbeans());
		// allowPoolSuspension: 是否允许连接池暂停 
		dataSource.setAllowPoolSuspension(hikariProperties.isAllowPoolSuspension());
		// leakDetectionThreshold: 泄漏检测阈值 ; leakDetectionThreshold is less than 2000ms or more than maxLifetime, disabling it
		dataSource.setLeakDetectionThreshold(hikariProperties.getLeakDetectionThreshold());
		
		if (StringUtils.isNotEmpty(hikariProperties.getJndiName())) {
			dataSource.setDataSourceJNDI(hikariProperties.getJndiName());
		}
		if (StringUtils.isNotEmpty(hikariProperties.getSchema())) {
			dataSource.setSchema(hikariProperties.getSchema());
		}
		if (StringUtils.isNotEmpty(hikariProperties.getCatalog())) {
			dataSource.setCatalog(hikariProperties.getCatalog());
		}
		
		if(!CollectionUtils.isEmpty(hikariProperties.getDataSourceProperties())) {
			dataSource.setDataSourceProperties(hikariProperties.getDataSourceProperties());
		}
		if(!CollectionUtils.isEmpty(hikariProperties.getHealthCheckProperties())) {
			dataSource.setHealthCheckProperties(hikariProperties.getHealthCheckProperties());
		}
		
		return dataSource;
	}

	@SuppressWarnings("unchecked")
	public static <T> T createDataSource(DataSourceProperties properties, Class<? extends DataSource> type) {
		return (T) properties.initializeDataSourceBuilder().type(type).build();
	}
 
	
}
