package com.zaxxer.hikari.spring.boot;

import java.util.NoSuchElementException;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(HikaricpWithMetricProperties.PREFIX)
public class HikaricpWithMetricProperties {

	public static final String PREFIX = "spring.datasource.hikari.metric";

/**
 * MetricType class.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
	public enum MetricType {

		DROPWIZARD("dropwizard"), MICROMETER("micrometer"), PROMETHEUS("prometheus");

		private final String metricType;

		MetricType(String metricType) {
			this.metricType = metricType;
		}
		/**
		 * <p>Get.</p>
		 * @return the string
		 */

		public String get() {
			return metricType;
		}
		/**
		 * <p>Equals.</p>
		 * @param metricType the metric type
		 * @return the boolean
		 */

		public boolean equals(MetricType metricType) {
			return this.compareTo(metricType) == 0;
		}
		/**
		 * <p>Equals.</p>
		 * @param metricType the metric type
		 * @return the boolean
		 */

		public boolean equals(String metricType) {
			return this.compareTo(MetricType.valueOfIgnoreCase(metricType)) == 0;
		}
		/**
		 * <p>Value of ignore case.</p>
		 * @param key the key
		 * @return the static  metric type
		 */

		public static MetricType valueOfIgnoreCase(String key) {
			for (MetricType metricType : MetricType.values()) {
				if (metricType.get().equalsIgnoreCase(key)) {
					return metricType;
				}
			}
			throw new NoSuchElementException("Cannot found metricType with key '" + key + "'.");
		}

	}

	private boolean enabled = false;
	private MetricType type = MetricType.DROPWIZARD;
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
	/** Gets the type. */

	public MetricType getType() {
		return type;
	}
	/** Sets the type. */

	public void setType(MetricType type) {
		this.type = type;
	}

}
