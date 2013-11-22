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

package org.scrutmydocs.webapp.service.jira;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.scrutmydocs.webapp.api.jira.data.JiraIssueComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.scrutmydocs.webapp.constant.SMDSearchProperties.JIRA_COMPONENT_INDEX;
import static org.scrutmydocs.webapp.constant.SMDSearchProperties.JIRA_ISSUE_COMMENT_TYPE;

/**
 * TODO Can't be tested for the moment, because either of JIRA River configuration or missing processing
 *
 * @author Johann NG SING KWONG
 */
@Component
public class JiraIssueCommentService {
	private static final long serialVersionUID = 1L;

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	@Autowired Client client;
	
	//@SuppressWarnings("unchecked")
	public JiraIssueComment get(String index, String type, String id) {
		GetResponse responseEs = client.prepareGet(index, type, id).execute().actionGet();
		if (!responseEs.isExists()) {
			return null;
		}
		JiraIssueComment response = new JiraIssueComment();
		StringBuilder sb = new StringBuilder();
		response.setId(responseEs.getId());
		response.setIndex(responseEs.getIndex());
		response.setType(responseEs.getType());
		
		Map<String, Object> jira_issue_comment_data = (Map<String, Object>) responseEs.getSource();
			response.setCommentsDocumentUrl((String) jira_issue_comment_data.get("document_url"));
			response.setCommentsCommentId((String) jira_issue_comment_data.get("comment_id"));
			response.setCommentsCommentBody((String) jira_issue_comment_data.get("comment_body"));
			response.setCommentsCommentCreated((String) jira_issue_comment_data.get("comment_created"));
			response.setCommentsCommentUpdated((String) jira_issue_comment_data.get("comment_updated"));
			
			Map<String, Object> comment_updater_data = (Map<String, Object>) jira_issue_comment_data.get("comment_updater");
			response.setCommentsCommentUpdaterDisplayName((String) comment_updater_data.get("display_name"));
			response.setCommentsCommentUpdaterUsername((String) comment_updater_data.get("username"));
			response.setCommentsCommentUpdaterEmailAddress((String) comment_updater_data.get("email_address"));

			Map<String, Object> comment_author_data = (Map<String, Object>) jira_issue_comment_data.get("comment_author");
			response.setCommentsCommentAuthorDisplayName((String) comment_author_data.get("display_name"));
			response.setCommentsCommentAuthorUsername((String) comment_author_data.get("username"));
			response.setCommentsCommentAuthorEmailAddress((String) comment_author_data.get("email_address"));

		return response;
	}
	
	public boolean delete(String index, String type, String id) {
		if (index == null) {
			index = JIRA_COMPONENT_INDEX;
		}
		if (type == null) {
			type = JIRA_ISSUE_COMMENT_TYPE;
		}

		DeleteResponse response = client.prepareDelete(index, type, index)
				.execute().actionGet();

		return response.isNotFound();
	}

}
