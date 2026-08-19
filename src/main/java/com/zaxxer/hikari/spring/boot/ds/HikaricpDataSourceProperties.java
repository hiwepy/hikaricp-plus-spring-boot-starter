package com.zaxxer.hikari.spring.boot.ds;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Properties;

/**
 * Configuration properties.
 * <p>Binds to the application property prefix and provides
 * customizable settings.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class HikaricpDataSourceProperties {

	/**
	 * 配置这个属性的意义在于，如果存在多个the data source，监控的时候可以通过名字来区分开来。如果没有配置，将会生成一个名字，格式是：”DataSource-” +
	 * System.identityHashCode(this)
	 */
	protected String name;
	/** jndiName: The JNDI name for the data source */
	private String jndiName;
	/** poolName: The connection pool name */
	private String poolName;
	/** schema: The schema for the database connection */
	private String schema;
	/** catalog: The catalog for the database connection */
	private String catalog;

	/** 基本属性 url、user、password */

	/** driverClassName: The JDBC driver class name */
	protected String driverClassName;
	/** jdbcUrl: The JDBC URL for the database connection，不同数据库不一样 */
	protected String jdbcUrl;
	/** username: The username for the database connection */
	protected String username;
	/** password: The password for the database connection */
	protected String password;

	/** Hikari 连接池参数 */

	/** initializationFailTimeout: Connection pool initialization failure timeout in milliseconds；单位 (毫秒) */
	private long initializationFailTimeout = 1;
	/** minIdle: Minimum number of idle connections in the pool */
	private int minIdle = 5;
	/** maxPoolSize: Maximum number of connections in the pool */
	protected Integer maxPoolSize = 50;
	/** maxLifetime: Maximum lifetime of a connection in milliseconds */
	private long maxLifetime = MINUTES.toMillis(30);

	/** connectionInitSql: SQL statement executed to initialize a connection before adding it to the pool,在连接加入连接池前执行 */
	private String connectionInitSql = "";
	/** connectionTestQuery: SQL statement used to test connection validity,当执行连接检查时执行 */
	private String connectionTestQuery = "SELECT 1";
	/** connectionTimeout: Connection acquisition timeout in milliseconds；单位 (毫秒) */
	private long connectionTimeout = SECONDS.toMillis(30);
	/** validationTimeout: Connection validation timeout in milliseconds；单位 (毫秒) */
	private long validationTimeout = SECONDS.toMillis(5);
	/** idleTimeout: Maximum idle time for a connection before release, in milliseconds，当一个连接超出该时间，会被释放；单位 (毫秒) */
	private long idleTimeout = MINUTES.toMillis(10);
	/** transactionIsolationName: Transaction isolation level name */
	private String transactionIsolationName;
	/** autoCommit: Whether to auto-commit transactions */
	private boolean autoCommit = true;
	/** readOnly: Whether the connection is read-only */
	private boolean readOnly = false;
	/** isolateInternalQueries: Whether to isolate internal queries in their own transaction */
	private boolean isolateInternalQueries;
	/** registerMbeans: Whether to register JMX monitoring beans */
	private boolean registerMbeans;
	/** allowPoolSuspension: Whether to allow the connection pool to be suspended */
	private boolean allowPoolSuspension;
	/**
	 * leakDetectionThreshold: Leak detection threshold in milliseconds ; leakDetectionThreshold is less than 2000ms
	 * or more than maxLifetime, disabling it
	 */
	private long leakDetectionThreshold = 0;
	/** dataSourceProperties: DataSource initialization properties */
	private Properties dataSourceProperties = new Properties();
	/** healthCheckProperties: Health check properties */
	private Properties healthCheckProperties = new Properties();
	/** Gets the name. */


	public String getName() {
		return name;
	}
	/** Sets the name. */

	public void setName(String name) {
		this.name = name;
	}
	/** Gets the jndi name. */

	public String getJndiName() {
		return jndiName;
	}
	/** Sets the jndi name. */

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}
	/** Gets the pool name. */

	public String getPoolName() {
		return poolName;
	}
	/** Sets the pool name. */

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
	/** Gets the schema. */

	public String getSchema() {
		return schema;
	}
	/** Sets the schema. */

	public void setSchema(String schema) {
		this.schema = schema;
	}
	/** Gets the catalog. */

	public String getCatalog() {
		return catalog;
	}
	/** Sets the catalog. */

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	/** Gets the driver class name. */

	public String getDriverClassName() {
		return driverClassName;
	}
	/** Sets the driver class name. */

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	/** Gets the jdbc url. */

	public String getJdbcUrl() {
		return jdbcUrl;
	}
	/** Sets the jdbc url. */

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	/** Gets the username. */

	public String getUsername() {
		return username;
	}
	/** Sets the username. */

	public void setUsername(String username) {
		this.username = username;
	}
	/** Gets the password. */

	public String getPassword() {
		return password;
	}
	/** Sets the password. */

	public void setPassword(String password) {
		this.password = password;
	}
	/** Gets the initialization fail timeout. */

	public long getInitializationFailTimeout() {
		return initializationFailTimeout;
	}
	/** Sets the initialization fail timeout. */

	public void setInitializationFailTimeout(long initializationFailTimeout) {
		this.initializationFailTimeout = initializationFailTimeout;
	}
	/** Gets the min idle. */

	public int getMinIdle() {
		return minIdle;
	}
	/** Sets the min idle. */

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}
	/** Gets the max pool size. */

	public Integer getMaxPoolSize() {
		return maxPoolSize;
	}
	/** Sets the max pool size. */

	public void setMaxPoolSize(Integer maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}
	/** Gets the max lifetime. */

	public long getMaxLifetime() {
		return maxLifetime;
	}
	/** Sets the max lifetime. */

	public void setMaxLifetime(long maxLifetime) {
		this.maxLifetime = maxLifetime;
	}
	/** Gets the connection init sql. */

	public String getConnectionInitSql() {
		return connectionInitSql;
	}
	/** Sets the connection init sql. */

	public void setConnectionInitSql(String connectionInitSql) {
		this.connectionInitSql = connectionInitSql;
	}
	/** Gets the connection test query. */

	public String getConnectionTestQuery() {
		return connectionTestQuery;
	}
	/** Sets the connection test query. */

	public void setConnectionTestQuery(String connectionTestQuery) {
		this.connectionTestQuery = connectionTestQuery;
	}
	/** Gets the connection timeout. */

	public long getConnectionTimeout() {
		return connectionTimeout;
	}
	/** Sets the connection timeout. */

	public void setConnectionTimeout(long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	/** Gets the validation timeout. */

	public long getValidationTimeout() {
		return validationTimeout;
	}
	/** Sets the validation timeout. */

	public void setValidationTimeout(long validationTimeout) {
		this.validationTimeout = validationTimeout;
	}
	/** Gets the idle timeout. */

	public long getIdleTimeout() {
		return idleTimeout;
	}
	/** Sets the idle timeout. */

	public void setIdleTimeout(long idleTimeout) {
		this.idleTimeout = idleTimeout;
	}
	/** Gets the transaction isolation name. */

	public String getTransactionIsolationName() {
		return transactionIsolationName;
	}
	/** Sets the transaction isolation name. */

	public void setTransactionIsolationName(String transactionIsolationName) {
		this.transactionIsolationName = transactionIsolationName;
	}
	/**
	 * <p>Is auto commit.</p>
	 * @return the boolean
	 */

	public boolean isAutoCommit() {
		return autoCommit;
	}
	/** Sets the auto commit. */

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}
	/**
	 * <p>Is read only.</p>
	 * @return the boolean
	 */

	public boolean isReadOnly() {
		return readOnly;
	}
	/** Sets the read only. */

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	/**
	 * <p>Is isolate internal queries.</p>
	 * @return the boolean
	 */

	public boolean isIsolateInternalQueries() {
		return isolateInternalQueries;
	}
	/** Sets the isolate internal queries. */

	public void setIsolateInternalQueries(boolean isolateInternalQueries) {
		this.isolateInternalQueries = isolateInternalQueries;
	}
	/**
	 * <p>Is register mbeans.</p>
	 * @return the boolean
	 */

	public boolean isRegisterMbeans() {
		return registerMbeans;
	}
	/** Sets the register mbeans. */

	public void setRegisterMbeans(boolean registerMbeans) {
		this.registerMbeans = registerMbeans;
	}
	/**
	 * <p>Is allow pool suspension.</p>
	 * @return the boolean
	 */

	public boolean isAllowPoolSuspension() {
		return allowPoolSuspension;
	}
	/** Sets the allow pool suspension. */

	public void setAllowPoolSuspension(boolean allowPoolSuspension) {
		this.allowPoolSuspension = allowPoolSuspension;
	}
	/** Gets the leak detection threshold. */

	public long getLeakDetectionThreshold() {
		return leakDetectionThreshold;
	}
	/** Sets the leak detection threshold. */

	public void setLeakDetectionThreshold(long leakDetectionThreshold) {
		this.leakDetectionThreshold = leakDetectionThreshold;
	}
	/** Gets the data source properties. */

	public Properties getDataSourceProperties() {
		return dataSourceProperties;
	}
	/** Sets the data source properties. */

	public void setDataSourceProperties(Properties dataSourceProperties) {
		this.dataSourceProperties = dataSourceProperties;
	}
	/** Gets the health check properties. */

	public Properties getHealthCheckProperties() {
		return healthCheckProperties;
	}
	/** Sets the health check properties. */

	public void setHealthCheckProperties(Properties healthCheckProperties) {
		this.healthCheckProperties = healthCheckProperties;
	}

}