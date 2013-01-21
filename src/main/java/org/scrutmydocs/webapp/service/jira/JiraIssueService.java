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

import java.util.ArrayList;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.scrutmydocs.webapp.api.common.RestAPIException;
import org.scrutmydocs.webapp.api.jira.data.JiraIssue;
import static org.scrutmydocs.webapp.constant.SMDSearchProperties.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * JiraIssueService
 * TODO push not implemented
 *
 * @author Johann NG SING KWONG
 */
@Component
public class JiraIssueService {
	private static final long serialVersionUID = 1L;

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	@Autowired Client client;
	

	public JiraIssue get(String index, String type, String id) {
		GetResponse responseEs = client.prepareGet(index, type, id).execute().actionGet();
		if (!responseEs.isExists()) {
			return null;
		}
		JiraIssue response = new JiraIssue();
		StringBuilder sb = new StringBuilder();
		response.setId(responseEs.getId());
		response.setIndex(responseEs.getIndex());
		response.setType(responseEs.getType());
		
		Map<String, Object> jira_issue_data = (Map<String, Object>) responseEs.getSource();
		
		response.setProjectKey((String) jira_issue_data.get("project_key"));
		response.setIssueKey((String) jira_issue_data.get("issue_key"));
		response.setDocumentUrl((String) jira_issue_data.get("document_url"));
		response.setSummary((String) jira_issue_data.get("summary"));
		response.setProjectName((String) jira_issue_data.get("project_name"));
		response.setIssueType((String) jira_issue_data.get("issue_type"));
		response.setStatus((String) jira_issue_data.get("status"));
		
		if (!responseEs.getSource().get("fix_versions").toString().isEmpty()) {
			//TODO not tested yet
			ArrayList<Map<String, Object>> fixversions_array = (ArrayList<Map<String, Object>>) responseEs.getSource().get("fix_versions");
			ArrayList<String> fixversions_list = new ArrayList<String>();
			for (int i=0;i<fixversions_array.size();i++){
				fixversions_list.add((String) fixversions_array.get(i).get("name"));
			}
			response.setComponentsList(fixversions_list);
		}
				
		response.setCreated((String) jira_issue_data.get("created"));
		response.setUpdated((String) jira_issue_data.get("updated"));
		response.setResolutiondate((String) jira_issue_data.get("resolutiondate"));
		response.setDescription((String) jira_issue_data.get("description"));


		if (!responseEs.getSource().get("labels").toString().isEmpty()) {
			response.setLabelsList((ArrayList<String>) responseEs.getSource().get("labels"));
		}

		Map<String, Object> assignee_data = (Map<String, Object>) responseEs.getSource().get("assignee");
		response.setAssigneeDisplayName((String) assignee_data.get("display_name"));
		response.setAssigneeUsername((String) assignee_data.get("username"));
		response.setAssigneeEmailAddress((String) assignee_data.get("email_address"));

		Map<String, Object> reporter_data = (Map<String, Object>) responseEs.getSource().get("reporter");
		response.setReporterDisplayName((String) reporter_data.get("display_name"));
		response.setReporterUsername((String) reporter_data.get("username"));
		response.setReporterEmailAddress((String) reporter_data.get("email_address"));
		
		if (!responseEs.getSource().get("components").toString().isEmpty()) {
			ArrayList<Map<String, Object>> components_array = (ArrayList<Map<String, Object>>) responseEs.getSource().get("components");
			ArrayList<String> components_list = new ArrayList<String>();
			for (int i=0;i<components_array.size();i++){
				components_list.add((String) components_array.get(i).get("name"));
			}
			response.setComponentsList(components_list);
		}
		
		//TODO to refactor comment processing, for the moment only 1rst comment is processed
		if (responseEs.getSource().get("comments") != null) {
			ArrayList<Map<String, Object>> comments_data = (ArrayList<Map<String, Object>>) responseEs.getSource().get("comments");
			response.setCommentsDocumentUrl((String) comments_data.get(0).get("document_url"));
			response.setCommentsCommentId((String) comments_data.get(0).get("comment_id"));
			response.setCommentsCommentBody((String) comments_data.get(0).get("comment_body"));
			response.setCommentsCommentCreated((String) comments_data.get(0).get("comment_created"));
			response.setCommentsCommentUpdated((String) comments_data.get(0).get("comment_updated"));
			
			Map<String, Object> comment_updater_data = (Map<String, Object>) comments_data.get(0).get("comment_updater");
			response.setCommentsCommentUpdaterDisplayName((String) comment_updater_data.get("display_name"));
			response.setCommentsCommentUpdaterUsername((String) comment_updater_data.get("username"));
			response.setCommentsCommentUpdaterEmailAddress((String) comment_updater_data.get("email_address"));

			Map<String, Object> comment_author_data = (Map<String, Object>) comments_data.get(0).get("comment_author");
			response.setCommentsCommentAuthorDisplayName((String) comment_author_data.get("display_name"));
			response.setCommentsCommentAuthorUsername((String) comment_author_data.get("username"));
			response.setCommentsCommentAuthorEmailAddress((String) comment_author_data.get("email_address"));
		}
		return response;
	}
	
	public boolean delete(String index, String type, String id) {
		if (index == null) {
			index = JIRA_COMPONENT_INDEX;
		}
		if (type == null) {
			type = JIRA_ISSUE_TYPE;
		}

		DeleteResponse response = client.prepareDelete(index, type, index)
				.execute().actionGet();

		return response.isNotFound();
	}

    /**
     * Not fully implemented.
     * TODO Need to think of a simple way where to push jira issue date fields with JIRA RIVER date format
     */
//	public JiraIssue push(JiraIssue jiraissue) throws RestAPIException {
//		if (logger.isDebugEnabled()) logger.debug("push({})", jiraissue);
//		if (jiraissue == null)
//			return null;
//
//		if (jiraissue.getIndex() == null || jiraissue.getIndex().isEmpty()) {
//			jiraissue.setIndex(JIRA_COMPONENT_INDEX);
//		}
//		if (jiraissue.getType() == null || jiraissue.getType().isEmpty()) {
//			jiraissue.setType(JIRA_ISSUE_TYPE);
//		}
//		try {
//			IndexResponse response = client
//					.prepareIndex(jiraissue.getIndex(), jiraissue.getType(),
//							jiraissue.getId())
//					.setSource(
//							jsonBuilder()
//									.startObject()
//									.field("issue_key", jiraissue.getIssueKey())
//									.field("postDate", new Date())
//									.startObject("file")
//									.field("_content_type",
//											jiraissue.getContentType())
//									.field("_name", jiraissue.getName())
//									.field("content", jiraissue.getContent())
//									.endObject().endObject()).execute()
//					.actionGet();
//
//			jiraissue.setId(response.getId());
//		} catch (Exception e) {
//			logger.warn("Can not index jiraissue {}", jiraissue.getIssueKey());
//			throw new RestAPIException("Can not index jiraissue : "+ jiraissue.getIssueKey() + ": "+e.getMessage());
//		}
//
//		if (logger.isDebugEnabled()) logger.debug("/push()={}", jiraissue);
//		return jiraissue;
//	}

}
