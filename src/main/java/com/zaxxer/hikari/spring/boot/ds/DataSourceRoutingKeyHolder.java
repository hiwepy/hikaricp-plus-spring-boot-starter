package com.zaxxer.hikari.spring.boot.ds;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Holds the current dynamic datasource lookup key for the active thread.
 */
public final class DataSourceRoutingKeyHolder {

	public static final String MASTER_DATASOURCE = "master";
	public static final Set<Object> dataSourceKeys = Collections.newSetFromMap(new ConcurrentHashMap<Object, Boolean>());

	private static final ThreadLocal<String> DATA_SOURCE_KEY_HOLDER = new ThreadLocal<String>();

	private DataSourceRoutingKeyHolder() {
	}

	public static String getDataSourceKey() {
		return DATA_SOURCE_KEY_HOLDER.get();
	}

	public static void setDataSourceKey(String dataSourceKey) {
		if (Objects.isNull(dataSourceKey)) {
			clearDataSourceKey();
			return;
		}
		DATA_SOURCE_KEY_HOLDER.set(dataSourceKey);
	}

	public static void clearDataSourceKey() {
		DATA_SOURCE_KEY_HOLDER.remove();
	}

}
