package org.scrutmydocs.webapp.api.jira.data;

import org.scrutmydocs.webapp.api.common.RestAPIException;
import org.scrutmydocs.webapp.api.common.data.RestResponse;
import org.scrutmydocs.webapp.api.jira.data.JiraIssue;

public class RestResponseJiraIssue extends RestResponse<JiraIssue>{
	private static final long serialVersionUID = 1L;
	//renvoyer un string
	public RestResponseJiraIssue(JiraIssue issue) {
		super(issue);
	}

	public RestResponseJiraIssue() {
		super();
	}

	public RestResponseJiraIssue(RestAPIException e) {
		super(e);
	}
}
