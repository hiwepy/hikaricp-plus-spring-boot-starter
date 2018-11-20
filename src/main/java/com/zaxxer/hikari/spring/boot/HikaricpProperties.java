package com.zaxxer.hikari.spring.boot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.zaxxer.hikari.spring.boot.ds.HikaricpDataSourceProperties;

@ConfigurationProperties(HikaricpProperties.PREFIX)
public class HikaricpProperties extends HikaricpDataSourceProperties {

	public static final String PREFIX = "spring.datasource.hikari";

	/**
	 * Enable Hikari.
	 */
	private boolean enabled = false;
	/** 动态数据源连接信息 */
	private List<HikaricpDataSourceProperties> slaves = new ArrayList<HikaricpDataSourceProperties>();

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<HikaricpDataSourceProperties> getSlaves() {
		return slaves;
	}

	public void setSlaves(List<HikaricpDataSourceProperties> slaves) {
		this.slaves = slaves;
	}

}