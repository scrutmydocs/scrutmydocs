/*
 * Licensed to scrutmydocs.org (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.scrutmydocs.webapp.service.settings.rivers;

import com.google.common.base.Predicate;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.indices.IndexMissingException;
import org.scrutmydocs.webapp.api.settings.rivers.abstractfs.data.AbstractFSRiver;
import org.scrutmydocs.webapp.api.settings.rivers.basic.data.BasicRiver;
import org.scrutmydocs.webapp.util.ESHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;


@Component
public class RiverService implements Serializable {
	private static final long serialVersionUID = 1L;

	private ESLogger logger = Loggers.getLogger(getClass().getName());
	
	@Autowired Client client;

    public static boolean awaitBusy(Predicate<?> breakPredicate, long maxWaitTime, TimeUnit unit) throws InterruptedException {
        long maxTimeInMillis = TimeUnit.MILLISECONDS.convert(maxWaitTime, unit);
        long iterations = Math.max(Math.round(Math.log10(maxTimeInMillis) / Math.log10(2)), 1);
        long timeInMillis = 1;
        long sum = 0;
        for (int i = 0; i < iterations; i++) {
            if (breakPredicate.apply(null)) {
                return true;
            }
            sum += timeInMillis;
            Thread.sleep(timeInMillis);
            timeInMillis *= 2;
        }
        timeInMillis = maxTimeInMillis - sum;
        Thread.sleep(Math.max(timeInMillis, 0));
        return breakPredicate.apply(null);
    }

	/**
	 * Check if the river exists and if it's started
	 * @param river
	 */
	public boolean checkState(final BasicRiver river) {
		if (logger.isDebugEnabled()) logger.debug("checkState({})", river);
		// We only check the river if you provide its definition
		if (river == null) return false;
		
		try {
            // With elasticsearch 1.x, rivers could take some time to be created on nodes
            // So we need to wait a little before stating the river has not been started
            boolean exists = awaitBusy(new Predicate<Object>() {
                @Override
                public boolean apply(Object input) {
                    boolean status = false;
                    try {
                        status = client
                                .prepareGet("_river", river.getId(), "_status")
                                .get().isExists();
                    } catch (IndexMissingException e) {
                        // We can ignore that one
                    }
                    return status;
                }
            // TODO We should try to have a better handling here as when a river does not run
            // users can have a little delay for displaying settings
            }, 500, TimeUnit.MILLISECONDS);

            return exists;
		} catch (Exception e) {
			logger.warn("checkState({}) : Exception raised : {}", river, e.getClass());
			if (logger.isDebugEnabled()) logger.debug("- Exception stacktrace :", e);
		}
		if (logger.isDebugEnabled()) logger.debug("/checkState({})", river);
	
		return false;
	}

	/**
	 * Start a river
	 * @param river The river to start
	 * @param xb JSON River definition
	 */
	public void start(BasicRiver river, XContentBuilder xb) {
		if (logger.isDebugEnabled()) logger.debug("add({})", river);
		
		// If our river is a FS River for document, we can manage the index creation
		if (river instanceof AbstractFSRiver) {
			AbstractFSRiver fsriver = (AbstractFSRiver) river;
			createIndexIfNeeded(fsriver);
		}

		try {
			client.prepareIndex("_river", river.getId(), "_meta").setRefresh(true).setSource(xb)
					.execute().isDone();
			
			boolean riverStarted = false;
			int nbChecks = 0;
			
			// We try 30 times before stopping
			while (!riverStarted && nbChecks++ < 30) {
				riverStarted = checkState(river);
				
				// We wait for 1 second
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			logger.warn("add({}) : Exception raised : {}", river, e.getClass());
			if (logger.isDebugEnabled()) logger.debug("- Exception stacktrace :", e);
		}
		if (logger.isDebugEnabled()) logger.debug("/add({})", river);
	}
	
	/**
	 * Stop a running river
	 * @param river
	 */
	public void stop(BasicRiver river) {
		if (logger.isDebugEnabled()) logger.debug("delete({})", river);
		if (river == null) return;
		
		try {
			client.admin().indices().prepareDeleteMapping("_river").setType(river.getId()).execute().actionGet();
		} catch (Exception e) {
			logger.warn("delete({}) : Exception raised : {}", river, e.getClass());
			if (logger.isDebugEnabled()) logger.debug("- Exception stacktrace :", e);
		}
		if (logger.isDebugEnabled()) logger.debug("/delete({})", river);
	}

	/**
	 * Stop all rivers
	 */
	public void stop() {
		if (logger.isDebugEnabled()) logger.debug("stop()");
        CloseIndexResponse response = client.admin().indices().prepareClose("_river").get();
        if (!response.isAcknowledged()) {
			logger.warn("stop() : Pb when closing rivers.");
		}
		if (logger.isDebugEnabled()) logger.debug("/stop()");
	}
	
	/**
	 * (Re)Start all rivers
	 */
	public void start() {
		if (logger.isDebugEnabled()) logger.debug("start()");
        OpenIndexResponse response = client.admin().indices().prepareOpen("_river").get();
		if (!response.isAcknowledged()) {
			logger.warn("start() : Pb when starting rivers.");
		}
		if (logger.isDebugEnabled()) logger.debug("/start()");
	}
	

	/**
	 * Create an index for the river if needed.
	 * <br>It helps to manage language analyzers
	 * @param fsriver
	 */
	public void createIndexIfNeeded(AbstractFSRiver fsriver) {
		if (logger.isDebugEnabled()) logger.debug("createIndexIfNeeded({})", fsriver);
		
		// We only add the river if the river is started
		if (fsriver == null || !fsriver.isStart()) return;
		
		ESHelper.createIndexIfNeeded(client, fsriver.getIndexname(), fsriver.getTypename(), fsriver.getAnalyzer());
		
		if (logger.isDebugEnabled()) logger.debug("/createIndexIfNeeded({})", fsriver);
	}
}
