package com.zaxxer.hikari.spring.boot;

import java.util.NoSuchElementException;

public class HikaricpWithMetricProperties {

	public static final String PREFIX = "spring.datasource.hikari.metric";

	public enum MetricType {

		DROPWIZARD("dropwizard"), MICROMETER("micrometer"), PROMETHEUS("prometheus");

		private final String metricType;

		MetricType(String metricType) {
			this.metricType = metricType;
		}

		public String get() {
			return metricType;
		}

		public boolean equals(MetricType metricType) {
			return this.compareTo(metricType) == 0;
		}

		public boolean equals(String metricType) {
			return this.compareTo(MetricType.valueOfIgnoreCase(metricType)) == 0;
		}

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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public MetricType getType() {
		return type;
	}

	public void setType(MetricType type) {
		this.type = type;
	}

}
