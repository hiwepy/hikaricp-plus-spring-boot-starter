/*
 * Copyright (c) 2017, hiwepy (https://github.com/easy-4-java).
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
package com.zaxxer.hikari.spring.boot.util;


import java.sql.Timestamp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import io.micrometer.core.instrument.Clock;


public class MicrometerSystemClock implements Clock {

    private final long period;
    private final AtomicLong now;

    private MicrometerSystemClock(long period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    /**
     * <p>Instance Holder.</p>
     *
     * @author <a href="https://github.com/loong10k">Loong Wan</a>
     * @since 1.0.0
     */
    private static class InstanceHolder {
        public static final MicrometerSystemClock INSTANCE = new MicrometerSystemClock(1);
    }
    /**
     * <p>Instance.</p>
     * @return the static  micrometer system clock
     */

    public static MicrometerSystemClock instance() {
        return InstanceHolder.INSTANCE;
    }
    /**
     * <p>Schedule clock updating.</p>
     */

    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            /**
             * <p>New thread.</p>
             * @param runnable the runnable
             * @return the thread
             */
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "System Clock");
                thread.setDaemon(true);
                return thread;
            }
        });
        scheduler.scheduleAtFixedRate(new Runnable() {
            /**
             * <p>Run.</p>
             */
            public void run() {
                now.set(System.currentTimeMillis());
            }
        }, period, period, TimeUnit.MILLISECONDS);
    }
    /**
     * <p>Current time millis.</p>
     * @return the long
     */

    private long currentTimeMillis() {
        return now.get();
    }
    /**
     * <p>Now.</p>
     * @return the static long
     */

    public static long now() {
        return instance().currentTimeMillis();
    }
	/**
	 * <p>Now date.</p>
	 * @return the static  string
	 */
    
	public static String nowDate() {
		return new Timestamp(instance().currentTimeMillis()).toString();
	}
	/**
	 * <p>Wall time.</p>
	 * @return the long
	 */

	@Override
	public long wallTime() {
		return now();
	}
	/**
	 * <p>Monotonic time.</p>
	 * @return the long
	 */

	@Override
	public long monotonicTime() {
		return now();
	}

}
