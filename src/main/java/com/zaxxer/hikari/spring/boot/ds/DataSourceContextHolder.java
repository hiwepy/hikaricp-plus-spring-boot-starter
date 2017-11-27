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
