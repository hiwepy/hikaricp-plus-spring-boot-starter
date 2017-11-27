/*
 * Copyright (c) 2010-2020, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.zaxxer.hikari.spring.boot.endpoints.health;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.util.ObjectUtils;

import com.zaxxer.hikari.spring.boot.ds.DynamicDataSource;

public class DataSourceCountHealthIndicator implements HealthIndicator {

	private Map<String, DataSource> dataSources;

	public DataSourceCountHealthIndicator(Map<String, DataSource> dataSources) {
		this.dataSources = dataSources;
	}

	@Override
	public Health health() {
		try {
			if (!ObjectUtils.isEmpty(dataSources)) {
				Iterator<Map.Entry<String, DataSource>> ite = dataSources.entrySet().iterator();
				while (ite.hasNext()) {
					Entry<String, DataSource> entry = ite.next();
					if(entry.getValue() instanceof DynamicDataSource) {
						DynamicDataSource dynamicDataSource = (DynamicDataSource) entry.getValue();
						
						long count = dynamicDataSource.getTargetDataSources().size();
						if (count >= 0) {
							return Health.up().withDetail("count", count).build();
						} else {
							return Health.unknown().withDetail("count", count).build();
						}
						
					}
				}
			}
			return Health.down().withDetail("count", 0).build();
		} catch (Exception e) {
			return Health.down(e).build();
		}

	}

}
