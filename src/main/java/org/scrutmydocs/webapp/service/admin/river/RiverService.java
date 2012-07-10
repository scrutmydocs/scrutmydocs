/*
 * Licensed to David Pilato and Malloum Laya (the "Authors") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Authors licenses this
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

package org.scrutmydocs.webapp.service.admin.river;

import java.io.Serializable;
import java.util.Map;

import org.elasticsearch.action.admin.indices.close.CloseIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.scrutmydocs.webapp.data.admin.river.FSRiver;
import org.scrutmydocs.webapp.data.admin.river.FSRiverHelper;
import org.scrutmydocs.webapp.util.ESHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RiverService implements Serializable {
	private static final long serialVersionUID = 1L;

	private ESLogger logger = Loggers.getLogger(getClass().getName());
	
	@Autowired Client client;

	/**
	 * Check if the river exists and if it's started
	 * @param river
	 */
	public boolean checkState(FSRiver river) {
		if (logger.isDebugEnabled()) logger.debug("checkState({})", river);
		// We only check the river if you provide its definition
		if (river == null) return false;
		
		try {
			GetResponse responseEs = client
					.prepareGet("_river", river.getId(), "_status")
					.execute().actionGet();
			if(!responseEs.isExists() )  {
				return false;
			}
			
			// We can also check if status is ok
			Map<String, Object> source = responseEs.sourceAsMap();
			if (source != null) {
				boolean status = FSRiverHelper.getSingleBooleanValue("ok", source);
				if (status) return true;
			}
			
			
			
		} catch (Exception e) {
			logger.warn("checkState({}) : Exception raised : {}", river, e.getClass());
			if (logger.isDebugEnabled()) logger.debug("- Exception stacktrace :", e);
		}
		if (logger.isDebugEnabled()) logger.debug("/checkState({})", river);
	
		return false;
	}
	
	/**
	 * Update (or add) a river
	 * @param river
	 */
	public void add(FSRiver river) {
		if (logger.isDebugEnabled()) logger.debug("add({})", river);
		// We only add the river if the river is started
		if (river == null || !river.isStart()) return;
		
		createIndexIfNeeded(river);
		
		XContentBuilder xb = FSRiverHelper.toXContent(river);		
		
		try {
			client.prepareIndex("_river", river.getId(), "_meta").setSource(xb)
					.execute().actionGet();
		} catch (Exception e) {
			logger.warn("add({}) : Exception raised : {}", river, e.getClass());
			if (logger.isDebugEnabled()) logger.debug("- Exception stacktrace :", e);
		}
		if (logger.isDebugEnabled()) logger.debug("/add({})", river);
	}
	
	/**
	 * Remove river
	 * @param river
	 */
	public void delete(FSRiver river) {
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
		CloseIndexRequestBuilder irb = new CloseIndexRequestBuilder(client.admin().indices());

		irb.setIndex("_river");
		CloseIndexResponse response = irb.execute().actionGet();
		
		if (!response.acknowledged()) {
			logger.warn("stop() : Pb when closing rivers.");
		}
		if (logger.isDebugEnabled()) logger.debug("/stop()");
	}
	
	/**
	 * (Re)Start all rivers
	 */
	public void start() {
		if (logger.isDebugEnabled()) logger.debug("start()");
		OpenIndexRequestBuilder irb = new OpenIndexRequestBuilder(client.admin().indices());

		irb.setIndex("_river");
		OpenIndexResponse response = irb.execute().actionGet();
		
		if (!response.acknowledged()) {
			logger.warn("start() : Pb when starting rivers.");
		}
		if (logger.isDebugEnabled()) logger.debug("/start()");
	}
	

	/**
	 * Create an index for the river if needed.
	 * <br>It helps to manage language analyzers
	 * @param river
	 */
	public void createIndexIfNeeded(FSRiver river) {
		if (logger.isDebugEnabled()) logger.debug("createIndexIfNeeded({})", river);
		
		// We only add the river if the river is started
		if (river == null || !river.isStart()) return;
		
		ESHelper.createIndexIfNeeded(client, river.getIndexname(), river.getTypename(), river.getAnalyzer());
		
		if (logger.isDebugEnabled()) logger.debug("/createIndexIfNeeded({})", river);
	}
}
