package com.zaxxer.hikari.spring.boot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.zaxxer.hikari.spring.boot.ds.HikaricpDataSourceProperties;

@ConfigurationProperties(HikaricpProperties.PREFIX)
/**
 * Configuration properties.
 * <p>Binds to the application property prefix and provides
 * customizable settings.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public class HikaricpProperties extends HikaricpDataSourceProperties {

	public static final String PREFIX = "spring.datasource.hikari";

	/**
	 * Enable Hikari.
	 */
	private boolean enabled = false;
	/**
	 * Enable Dynamic Routing.
	 */
	private boolean routable = false;
	/** 
	 * Datasource slaves 
	 */
	private List<HikaricpDataSourceProperties> slaves = new ArrayList<HikaricpDataSourceProperties>();

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isRoutable() {
		return routable;
	}

	public void setRoutable(boolean routable) {
		this.routable = routable;
	}

	public List<HikaricpDataSourceProperties> getSlaves() {
		return slaves;
	}

	public void setSlaves(List<HikaricpDataSourceProperties> slaves) {
		this.slaves = slaves;
	}

}