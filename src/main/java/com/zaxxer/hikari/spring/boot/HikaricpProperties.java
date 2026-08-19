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
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
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
	/**
	 * <p>Is enabled.</p>
	 * @return the boolean
	 */

	public boolean isEnabled() {
		return enabled;
	}
	/** Sets the enabled. */

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	/**
	 * <p>Is routable.</p>
	 * @return the boolean
	 */
	
	public boolean isRoutable() {
		return routable;
	}
	/** Sets the routable. */

	public void setRoutable(boolean routable) {
		this.routable = routable;
	}
	/** Gets the slaves. */

	public List<HikaricpDataSourceProperties> getSlaves() {
		return slaves;
	}
	/** Sets the slaves. */

	public void setSlaves(List<HikaricpDataSourceProperties> slaves) {
		this.slaves = slaves;
	}

}