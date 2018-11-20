package com.zaxxer.hikari.spring.boot.ds;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Properties;

public class HikaricpDataSourceProperties {

	/**
	 * 配置这个属性的意义在于，如果存在多个数据源，监控的时候可以通过名字来区分开来。如果没有配置，将会生成一个名字，格式是：”DataSource-” +
	 * System.identityHashCode(this)
	 */
	protected String name;
	/** jndiName: 数据库jndi名称 */
	private String jndiName;
	/** poolName: 连接池名称 */
	private String poolName;
	/** schema: 设置数据库连接的schema */
	private String schema;
	/** catalog: 设置数据库连接的catalog */
	private String catalog;

	/** 基本属性 url、user、password */

	/** driverClassName: 数据库驱动 */
	protected String driverClassName;
	/** jdbcUrl: 连接数据库的url，不同数据库不一样 */
	protected String jdbcUrl;
	/** username: 连接数据库的用户名 */
	protected String username;
	/** password: 连接数据库的密码 */
	protected String password;

	/** Hikari 连接池参数 */

	/** initializationFailTimeout: 连接池初始化失败超时时间；单位 (毫秒) */
	private long initializationFailTimeout = 1;
	/** minIdle: 连接池最小连接数量 */
	private int minIdle = 5;
	/** maxPoolSize: 连接池最大连接数量 */
	protected Integer maxPoolSize = 50;
	/** maxLifetime: 连接存活时间 */
	private long maxLifetime = MINUTES.toMillis(30);

	/** connectionInitSql: 连接初始化SQL语句,在连接加入连接池前执行 */
	private String connectionInitSql = "";
	/** connectionTestQuery: 连接有效性检查SQL语句,当执行连接检查时执行 */
	private String connectionTestQuery = "SELECT 1";
	/** connectionTimeout: 连接初始化超时时间；单位 (毫秒) */
	private long connectionTimeout = SECONDS.toMillis(30);
	/** validationTimeout: 连接检查超时时间；单位 (毫秒) */
	private long validationTimeout = SECONDS.toMillis(5);
	/** idleTimeout: 连接空闲超时时间，当一个连接超出该时间，会被释放；单位 (毫秒) */
	private long idleTimeout = MINUTES.toMillis(10);
	/** transactionIsolationName: 事务隔离名称 */
	private String transactionIsolationName;
	/** autoCommit: 是否自动提交事务 */
	private boolean autoCommit = true;
	/** readOnly: 是否只读 */
	private boolean readOnly = false;
	/** isolateInternalQueries: 是否内部查询事务隔离 */
	private boolean isolateInternalQueries;
	/** registerMbeans: 是否注册JMX监控参数输出 */
	private boolean registerMbeans;
	/** allowPoolSuspension: 是否允许连接池暂停 */
	private boolean allowPoolSuspension;
	/**
	 * leakDetectionThreshold: 泄漏检测阈值 ; leakDetectionThreshold is less than 2000ms
	 * or more than maxLifetime, disabling it
	 */
	private long leakDetectionThreshold = 0;
	/** dataSourceProperties: 连接池初始化参数 */
	private Properties dataSourceProperties = new Properties();
	/** healthCheckProperties: 连接池监控参数 */
	private Properties healthCheckProperties = new Properties();


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
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

	public long getInitializationFailTimeout() {
		return initializationFailTimeout;
	}

	public void setInitializationFailTimeout(long initializationFailTimeout) {
		this.initializationFailTimeout = initializationFailTimeout;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public Integer getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(Integer maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public long getMaxLifetime() {
		return maxLifetime;
	}

	public void setMaxLifetime(long maxLifetime) {
		this.maxLifetime = maxLifetime;
	}

	public String getConnectionInitSql() {
		return connectionInitSql;
	}

	public void setConnectionInitSql(String connectionInitSql) {
		this.connectionInitSql = connectionInitSql;
	}

	public String getConnectionTestQuery() {
		return connectionTestQuery;
	}

	public void setConnectionTestQuery(String connectionTestQuery) {
		this.connectionTestQuery = connectionTestQuery;
	}

	public long getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public long getValidationTimeout() {
		return validationTimeout;
	}

	public void setValidationTimeout(long validationTimeout) {
		this.validationTimeout = validationTimeout;
	}

	public long getIdleTimeout() {
		return idleTimeout;
	}

	public void setIdleTimeout(long idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	public String getTransactionIsolationName() {
		return transactionIsolationName;
	}

	public void setTransactionIsolationName(String transactionIsolationName) {
		this.transactionIsolationName = transactionIsolationName;
	}

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isIsolateInternalQueries() {
		return isolateInternalQueries;
	}

	public void setIsolateInternalQueries(boolean isolateInternalQueries) {
		this.isolateInternalQueries = isolateInternalQueries;
	}

	public boolean isRegisterMbeans() {
		return registerMbeans;
	}

	public void setRegisterMbeans(boolean registerMbeans) {
		this.registerMbeans = registerMbeans;
	}

	public boolean isAllowPoolSuspension() {
		return allowPoolSuspension;
	}

	public void setAllowPoolSuspension(boolean allowPoolSuspension) {
		this.allowPoolSuspension = allowPoolSuspension;
	}

	public long getLeakDetectionThreshold() {
		return leakDetectionThreshold;
	}

	public void setLeakDetectionThreshold(long leakDetectionThreshold) {
		this.leakDetectionThreshold = leakDetectionThreshold;
	}

	public Properties getDataSourceProperties() {
		return dataSourceProperties;
	}

	public void setDataSourceProperties(Properties dataSourceProperties) {
		this.dataSourceProperties = dataSourceProperties;
	}

	public Properties getHealthCheckProperties() {
		return healthCheckProperties;
	}

	public void setHealthCheckProperties(Properties healthCheckProperties) {
		this.healthCheckProperties = healthCheckProperties;
	}

}