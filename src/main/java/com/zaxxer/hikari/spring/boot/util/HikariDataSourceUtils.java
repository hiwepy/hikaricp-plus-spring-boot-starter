package com.zaxxer.hikari.spring.boot.util;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.util.CollectionUtils;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.spring.boot.ds.HikaricpDataSourceProperties;

/**
 * HikariDataSourceUtils class.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class HikariDataSourceUtils {
	/**
	 * <p>Create data source.</p>
	 * @param hikariProperties the hikari properties
	 * @return the static < t extends  data source>  hikari data source
	 */

	public static <T extends DataSource> HikariDataSource createDataSource(HikaricpDataSourceProperties hikariProperties) {
		
		DataSourceProperties tmProperties = new DataSourceProperties();
		
		tmProperties.setName(hikariProperties.getName());
		tmProperties.setType(com.zaxxer.hikari.HikariDataSource.class);
		// driverClassName : The JDBC driver class name 
		tmProperties.setDriverClassName(hikariProperties.getDriverClassName());
		// jdbcUrl: The JDBC URL for the database connection
		tmProperties.setUrl(hikariProperties.getJdbcUrl());
		// username: The username for the database connection
		tmProperties.setUsername(hikariProperties.getUsername());
		// password: The password for the database connection
		tmProperties.setPassword(hikariProperties.getPassword());
		
		// 创建 HikariDataSource the data source对象
		HikariDataSource dataSource = createDataSource(tmProperties, tmProperties.getType());
		// 配置 Hikarithe data source
		configureProperties(hikariProperties, dataSource);
		
		return dataSource;
	}
	/**
	 * <p>Create data source.</p>
	 * @param properties the properties
	 * @param type the type
	 * @return the static < t>  t
	 */

	@SuppressWarnings("unchecked")
	public static <T> T createDataSource(DataSourceProperties properties, Class<? extends DataSource> type) {
		return (T) properties.initializeDataSourceBuilder().type(type).build();
	}
	/**
	 * <p>Configure properties.</p>
	 * @param hikariProperties the hikari properties
	 * @param dataSource the data source
	 * @return the static void
	 */
	
	public static void configureProperties(HikaricpDataSourceProperties hikariProperties, HikariDataSource dataSource) {

		// Hikari 连接池参数
		
		// initializationFailTimeout: Connection pool initialization failure timeout in milliseconds；单位 (毫秒) 
		dataSource.setInitializationFailTimeout(hikariProperties.getInitializationFailTimeout());
		// minIdle: Minimum number of idle connections in the pool 
		dataSource.setMinimumIdle(hikariProperties.getMinIdle());
		// maxPoolSize: Maximum number of connections in the pool 
		dataSource.setMaximumPoolSize(hikariProperties.getMaxPoolSize());
		// maxLifetime: Maximum lifetime of a connection in milliseconds 
		dataSource.setMaxLifetime(hikariProperties.getMaxLifetime());

		if (StringUtils.isNotEmpty(hikariProperties.getConnectionInitSql())) {
			// connectionInitSql: SQL statement executed to initialize a connection before adding it to the pool,在连接加入连接池前执行 
			dataSource.setConnectionInitSql(hikariProperties.getConnectionInitSql());
			
		}
		if (StringUtils.isNotEmpty(hikariProperties.getConnectionTestQuery())) {
			// connectionTestQuery: SQL statement used to test connection validity,当执行连接检查时执行 
			dataSource.setConnectionTestQuery(hikariProperties.getConnectionTestQuery());
			// validationTimeout: Connection validation timeout in milliseconds；单位 (毫秒) 
			dataSource.setValidationTimeout(hikariProperties.getValidationTimeout());
		}
		
		// connectionTimeout: Connection acquisition timeout in milliseconds；单位 (毫秒) 
		dataSource.setConnectionTimeout(hikariProperties.getConnectionTimeout());
		// idleTimeout: Maximum idle time for a connection before release, in milliseconds，当一个连接超出该时间，会被释放；单位 (毫秒) 
		dataSource.setIdleTimeout(hikariProperties.getIdleTimeout());
		// transactionIsolationName: Transaction isolation level name 
		dataSource.setTransactionIsolation(hikariProperties.getTransactionIsolationName());
		// autoCommit: Whether to auto-commit transactions 
		dataSource.setAutoCommit(hikariProperties.isAutoCommit());
		// readOnly: Whether the connection is read-only 
		dataSource.setReadOnly(hikariProperties.isReadOnly());
		// isolateInternalQueries: Whether to isolate internal queries in their own transaction 
		dataSource.setIsolateInternalQueries(hikariProperties.isIsolateInternalQueries());
		// registerMbeans: Whether to register JMX monitoring beans 
		dataSource.setRegisterMbeans(hikariProperties.isRegisterMbeans());
		// allowPoolSuspension: Whether to allow the connection pool to be suspended 
		dataSource.setAllowPoolSuspension(hikariProperties.isAllowPoolSuspension());
		// leakDetectionThreshold: Leak detection threshold in milliseconds ; leakDetectionThreshold is less than 2000ms or more than maxLifetime, disabling it
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
		
	}
	
}
