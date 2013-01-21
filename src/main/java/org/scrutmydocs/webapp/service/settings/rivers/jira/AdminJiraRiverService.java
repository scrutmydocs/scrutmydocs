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

package org.scrutmydocs.webapp.service.settings.rivers.jira;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.scrutmydocs.webapp.api.settings.rivers.AbstractRiverHelper;
import org.scrutmydocs.webapp.api.settings.rivers.jira.data.JiraRiver;
import org.scrutmydocs.webapp.api.settings.rivers.jira.helper.JiraRiverHelper;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.scrutmydocs.webapp.service.settings.rivers.AdminRiverAbstractService;
import org.scrutmydocs.webapp.service.settings.rivers.RiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * River Service Implementation for Jira Rivers
 * @author Johann NG
 *
 */
@Component
public class AdminJiraRiverService extends AdminRiverAbstractService<JiraRiver> {
	private static final long serialVersionUID = 1L;
	private ESLogger logger = Loggers.getLogger(getClass().getName());


    @Autowired Client client;
	@Autowired RiverService riverService;
	
	@Override
	public AbstractRiverHelper<JiraRiver> getHelper() {
		return new JiraRiverHelper();
	}

	@Override
	public JiraRiver buildInstance() {
		return new JiraRiver();
	}


	/**
	 * Update (or add) a Jira river
	 * @param jirariver
	 */
    @Override
	public void update(JiraRiver jirariver) {
		if (logger.isDebugEnabled()) logger.debug("update({})", jirariver);
        try {
            riverService.createIndexIfNeeded(jirariver);
            XContentBuilder xb = ((JiraRiverHelper)this.getHelper()).toXContent(jirariver);

            client.prepareIndex(SMDSearchProperties.ES_META_INDEX, SMDSearchProperties.ES_META_RIVERS, jirariver.getId()).setSource(xb).setRefresh(true)
                    .execute().actionGet();

            if (logger.isDebugEnabled()) logger.debug("/update({})", jirariver);
        } catch (Exception e) {
            logger.error("Can't create pre-quisite JIRA index /update({})",e);
            e.printStackTrace();
        }
	}

    /**
     * Remove Jira River
     * @param jirariver JiraRiver
     * TODO : clean required index for JIRA River
     */
    @Override
    public void remove(JiraRiver jirariver) {
        if (logger.isDebugEnabled()) logger.debug("JIRA River remove({})", jirariver);

        // We stop the river if running
        if (riverService.checkState(jirariver)) {
            riverService.stop(jirariver);
        }

        deleteJiraIndex(jirariver.getJiraRiverActivityIndexName());
        deleteJiraIndex(jirariver.getIndexname());

        // We remove the river in the database
        client.prepareDelete(SMDSearchProperties.ES_META_INDEX, SMDSearchProperties.ES_META_RIVERS, jirariver.getId()).execute().actionGet();
        if (logger.isDebugEnabled()) logger.debug("/ JIRA River remove({})", jirariver);
    }

    /**
     * Delete JIRA River indexes
     */
    public void deleteJiraIndex(String indexName) {
        if (client.admin().indices().prepareExists(indexName).execute().actionGet().isExists()) {
            DeleteIndexRequest deleteIndexRequest = Requests.deleteIndexRequest(indexName);
            DeleteIndexResponse delete = client.admin().indices().delete(deleteIndexRequest).actionGet();
            if (!delete.isAcknowledged()) {
                logger.error("Index wasn't deleted");
            }
        }
    }


}
