package org.scrutmydocs.webapp.api.jira.data;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * JiraIssue
 * TODO Comments processing need to be refactor, only 1rst comment is processed
 *
 * @author Johann NG SING KWONG
 */
public class JiraIssue implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id = null;
	private String index = null;
	private String type = null;
	private String project_key = null;
	private String issue_key = null;
	private String document_url = null;
	private String summary = null;
	private String project_name = null;
	private String issue_type = null;
	private String status = null;
	private String created = null;
	private String updated = null;
	private String resolutiondate = null;
	private String description = null;
	private ArrayList<String> labels_list = null;
	private String assignee_display_name = null;
	private String assignee_username = null;
	private String assignee_email_address = null;
	private String reporter_display_name = null;
	private String reporter_username = null;
	private String reporter_email_address = null;
	private ArrayList<String> fix_version_list = null;
	private ArrayList<String> components_list = null;
	//TODO Comment processing need to be refactor, following fields can only be used for 1rst comment
	private String comments_document_url =  null;
	private String comments_comment_id =  null;
	private String comments_comment_body =  null;
	private String comments_comment_created =  null;
	private String comments_comment_updated =  null;
	private String comments_comment_updater_display_name =  null;
	private String comments_comment_updater_username =  null;
	private String comments_comment_updater_email_address =  null;
	private String comments_comment_author_display_name =  null;
	private String comments_comment_author_username =  null;
	private String comments_comment_author_email_address =  null;
	

	/**
	 * Default constructor
	 */
	public JiraIssue() {
	}

	/**
     * TODO Complete javadoc and add other JIRA Issue fields
	 * @param id Technical Unique ID for this document. May be null.
	 * @param index Index where will be stored your document (E.g.: mycompany)
	 * @param type Functionnal type of your document (E.g.: mybusinessunit)
	 * @param project_key Key of project in JIRA
	 * @param issue_key  Issue key from jira - also used as ID of document in the index, eg. `ORG-12` 
	 * @param document_url URL to show issue in JIRA GUI
	 */
	public JiraIssue(String id, String index, String type, String project_key,
			String issue_key, String document_url) {
		super();
		this.id = id;
		this.index = index;
		this.type = type;
		this.project_key = project_key;
		this.issue_key = issue_key;
		this.document_url = document_url;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the project_key
	 */
	public String getProjectKey() {
		return project_key;
	}
	/**
	 * @param project_key the project_key to set
	 */
	public void setProjectKey(String project_key) {
		this.project_key = project_key;
	}
	/**
	 * @return the issue_key
	 */
	public String getIssueKey() {
		return issue_key;
	}
	/**
	 * @param issue_key the issue_key to set
	 */
	public void setIssueKey(String issue_key) {
		this.issue_key = issue_key;
	}
	/**
	 * @return the document_url
	 */
	public String getDocumentUrl() {
		return document_url;
	}
	/**
	 * @param document_url the document_url to set
	 */
	public void setDocumentUrl(String document_url) {
		this.document_url = document_url;
	}
	
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getProjectName() {
		return project_name;
	}

	public void setProjectName(String project_name) {
		this.project_name = project_name;
	}

	public String getIssueType() {
		return issue_type;
	}

	public void setIssueType(String issue_type) {
		this.issue_type = issue_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getResolutiondate() {
		return resolutiondate;
	}

	public void setResolutiondate(String resolutiondate) {
		this.resolutiondate = resolutiondate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<String> getLabelsList() {
		return labels_list;
	}

	public void setLabelsList(ArrayList<String> labels_list) {
		this.labels_list = labels_list;
	}

	public String getAssigneeDisplayName() {
		return assignee_display_name;
	}

	public void setAssigneeDisplayName(String assignee_display_name) {
		this.assignee_display_name = assignee_display_name;
	}

	public String getAssigneeUsername() {
		return assignee_username;
	}

	public void setAssigneeUsername(String assignee_username) {
		this.assignee_username = assignee_username;
	}

	public String getAssigneeEmailAddress() {
		return assignee_email_address;
	}

	public void setAssigneeEmailAddress(String assignee_email_address) {
		this.assignee_email_address = assignee_email_address;
	}

	public String getReporterDisplayName() {
		return reporter_display_name;
	}

	public void setReporterDisplayName(String reporter_display_name) {
		this.reporter_display_name = reporter_display_name;
	}

	public String getReporterUsername() {
		return reporter_username;
	}

	public void setReporterUsername(String reporter_username) {
		this.reporter_username = reporter_username;
	}

	public String getReporterEmailAddress() {
		return reporter_email_address;
	}

	public void setReporterEmailAddress(String reporter_email_address) {
		this.reporter_email_address = reporter_email_address;
	}

	public ArrayList<String> getFixVersionList() {
		return fix_version_list;
	}

	public void setFixVersionList(ArrayList<String> fix_version_list) {
		this.fix_version_list = fix_version_list;
	}

	public ArrayList<String> getComponentsList() {
		return components_list;
	}

	public void setComponentsList(ArrayList<String> components_list) {
		this.components_list = components_list;
	}
	//TODO Comment processing need to be refactor, following getters/setters can only be used for 1rst comment
	public String getCommentsDocumentUrl() {
		return comments_document_url;
	}
	public void setCommentsDocumentUrl(String comments_document_url) {
		this.comments_document_url = comments_document_url;
	}
	public String getCommentsCommentId() {
		return comments_comment_id;
	}
	public void setCommentsCommentId(String comments_comment_id) {
		this.comments_comment_id = comments_comment_id;
	}
	public String getCommentsCommentBody() {
		return comments_comment_body;
	}
	public void setCommentsCommentBody(String comments_comment_body) {
		this.comments_comment_body = comments_comment_body;
	}
	public String getCommentsCommentCreated() {
		return comments_comment_created;
	}
	public void setCommentsCommentCreated(String comments_comment_created) {
		this.comments_comment_created = comments_comment_created;
	}
	public String getCommentsCommentUpdated() {
		return comments_comment_updated;
	}
	public void setCommentsCommentUpdated(String comments_comment_updated) {
		this.comments_comment_updated = comments_comment_updated;
	}
	public String getCommentsCommentUpdaterDisplayName() {
		return comments_comment_updater_display_name;
	}
	public void setCommentsCommentUpdaterDisplayName(
			String comments_comment_updater_display_name) {
		this.comments_comment_updater_display_name = comments_comment_updater_display_name;
	}
	public String getCommentsCommentUpdaterUsername() {
		return comments_comment_updater_username;
	}
	public void setCommentsCommentUpdaterUsername(
			String comments_comment_updater_username) {
		this.comments_comment_updater_username = comments_comment_updater_username;
	}
	public String getCommentsCommentUpdaterEmailAddress() {
		return comments_comment_updater_email_address;
	}
	public void setCommentsCommentUpdaterEmailAddress(
			String comments_comment_updater_email_address) {
		this.comments_comment_updater_email_address = comments_comment_updater_email_address;
	}
	public String getCommentsCommentAuthorDisplayName() {
		return comments_comment_author_display_name;
	}
	public void setCommentsCommentAuthorDisplayName(
			String comments_comment_author_display_name) {
		this.comments_comment_author_display_name = comments_comment_author_display_name;
	}
	public String getCommentsCommentAuthorUsername() {
		return comments_comment_author_username;
	}
	public void setCommentsCommentAuthorUsername(
			String comments_comment_author_username) {
		this.comments_comment_author_username = comments_comment_author_username;
	}
	public String getCommentsCommentAuthorEmailAddress() {
		return comments_comment_author_email_address;
	}
	public void setCommentsCommentAuthorEmailAddress(
			String comments_comment_author_email_address) {
		this.comments_comment_author_email_address = comments_comment_author_email_address;
	}
	
	//TODO Complete equals method
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof JiraIssue)) return false;
		
		JiraIssue jira_issue = (JiraIssue) obj;
		
		if (this.id != jira_issue.id && this.id != null && !this.id.equals(jira_issue.id)) return false;
		if (this.index != jira_issue.index && this.index != null && !this.index.equals(jira_issue.index)) return false;
		if (this.type != jira_issue.type && this.type != null && !this.type.equals(jira_issue.type)) return false;
		if (this.project_key != jira_issue.project_key && this.project_key != null && !this.project_key.equals(jira_issue.project_key)) return false;
		if (this.issue_key != jira_issue.issue_key && this.issue_key != null && !this.issue_key.equals(jira_issue.issue_key)) return false;
		if (this.document_url != jira_issue.document_url && this.document_url != null && !this.document_url.equals(jira_issue.document_url)) return false;

		return true;
	}
	//TODO complete toString method
	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("JiraIssue=[ id=");
		sbf.append(this.id);
		sbf.append(", index=");
		sbf.append(this.index);
		sbf.append(", type");
		sbf.append(this.type);
		sbf.append(", project_key=");
		sbf.append(this.project_key);
		sbf.append(", issue_key=");
		sbf.append(this.issue_key);
		sbf.append(" ]");
		return sbf.toString();
		// return StringTools.toString(this);
	}
}
