package org.scrutmydocs.webapp.api.jira.data;

/**
 * TODO Not fully functional
 *
 * @author Johann NG SING KWONG
 */
public class JiraIssueComment {
    private String id = null;
    private String index = null;
    private String type = null;
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
	
	public JiraIssueComment(){
		
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
}
