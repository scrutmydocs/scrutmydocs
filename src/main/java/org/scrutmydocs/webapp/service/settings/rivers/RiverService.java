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

import org.elasticsearch.action.admin.indices.close.CloseIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.scrutmydocs.webapp.api.settings.rivers.abstractfs.data.AbstractFSRiver;
import org.scrutmydocs.webapp.api.settings.rivers.basic.data.BasicRiver;
import org.scrutmydocs.webapp.api.settings.rivers.jira.data.JiraRiver;
import org.scrutmydocs.webapp.api.settings.rivers.fs.helper.FSRiverHelper;
import org.scrutmydocs.webapp.util.ESHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;


@Component
public class RiverService implements Serializable {
	private static final long serialVersionUID = 1L;

	private ESLogger logger = Loggers.getLogger(getClass().getName());
	
	@Autowired Client client;

	/**
	 * Check if the river exists and if it's started
	 * @param river
	 */
	public boolean checkState(BasicRiver river) {
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
			Map<String, Object> source = responseEs.getSourceAsMap();
			if (source != null) {
				Boolean status = FSRiverHelper.getSingleBooleanValue("ok", source);
				if (status == null) return false;
                return status;
			}
			
			
			
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
		// If our river is a Jira River for document, we can manage the index creation
		if (river instanceof JiraRiver) {
			JiraRiver jirariver = (JiraRiver) river;
			try {
				createIndexIfNeeded(jirariver);
			} catch (Exception e) {
				logger.warn("start() : Exception raised : {}", e.getClass());
				if (logger.isDebugEnabled()) logger.debug("- Exception stacktrace :", e);
			}
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
		CloseIndexRequestBuilder irb = new CloseIndexRequestBuilder(client.admin().indices());

		irb.setIndex("_river");
		CloseIndexResponse response = irb.execute().actionGet();
		
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
		OpenIndexRequestBuilder irb = new OpenIndexRequestBuilder(client.admin().indices());

		irb.setIndex("_river");
		OpenIndexResponse response = irb.execute().actionGet();
		
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
        
    /**
	 * Create required indexes for the jira river if needed.
	 * @param jirariver
     * @throws Exception 
	 */
	public void createIndexIfNeeded(JiraRiver jirariver) throws Exception {
		if (logger.isDebugEnabled()) logger.debug("createIndexIfNeeded({})", jirariver);
		
		// We only add the river if the river is started
		if (jirariver == null || !jirariver.isStart()) return;

        ESHelper.createIndexIfNeededNoMapping(client,jirariver.getIndexname());
        ESHelper.createIndexIfNeededNoMapping(client,jirariver.getJiraRiverActivityIndexName());
        ESHelper.createJiraIndexMapping(client,jirariver.getIndexname(),jirariver.getJiraIssueCommentType(),jirariver.getAnalyzer(),true);
        ESHelper.createJiraIndexMapping(client,jirariver.getJiraRiverActivityIndexName(),jirariver.getJiraRiverUpdateType(),jirariver.getAnalyzer(),false);

		if (logger.isDebugEnabled()) logger.debug("/createIndexIfNeeded({})", jirariver);
	}

}
