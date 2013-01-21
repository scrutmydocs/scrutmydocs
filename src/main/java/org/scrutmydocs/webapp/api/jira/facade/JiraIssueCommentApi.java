package org.scrutmydocs.webapp.api.jira.facade;

import org.scrutmydocs.webapp.api.common.data.Api;
import org.scrutmydocs.webapp.api.common.facade.CommonBaseApi;
import org.scrutmydocs.webapp.api.jira.data.JiraIssueComment;
import org.scrutmydocs.webapp.service.jira.JiraIssueCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.scrutmydocs.webapp.constant.SMDSearchProperties.JIRA_COMPONENT_INDEX;
import static org.scrutmydocs.webapp.constant.SMDSearchProperties.JIRA_ISSUE_COMMENT_TYPE;


/**
 * JiraIssueComment RESTFul API
 * TODO Can't be tested for the moment, because either of JIRA River configuration or missing processing
 * @author Johann NG SING KWONG
 *
 */
@Controller
@RequestMapping("/1/jiracomment")
public class JiraIssueCommentApi extends CommonBaseApi {
	
	@Autowired
	JiraIssueCommentService jiraIssueCommentService;
	
	@Override
	public Api[] helpApiList() {
		Api[] apis = new Api[7];
		//apis[0] = new Api("/1/jiracomment", "POST", "Add a jira document to the search engine");  Not implemented for the moment
		apis[1] = new Api("/1/jiracomment/{id}", "DELETE", "Delete a jira document in the default index/type (my_jira_index/jira_issue_comment)");
		apis[2] = new Api("/1/jiracomment/{index}/{id}", "DELETE", "Delete a jira document in the default type (jira_issue_comment)");
		apis[3] = new Api("/1/jiracomment/{index}/{type}/{id}", "DELETE", "Delete a jira issue comment document ");
		apis[4] = new Api("/1/jiracomment/{id}", "GET", "Get a jira document in the default index/type (my_jira_index/jira_issue_comment)");
		apis[5] = new Api("/1/jiracomment/{index}/{id}", "GET", "Get a jira issue comment document in a specific index with default type (jira_issue_comment)");
		apis[6] = new Api("/1/jiracomment/{index}/{type}/{id}", "GET", "Get a jira issue comment document in a specific index/type");
		return apis;
	}

	@Override
	public String helpMessage() {
		// TODO Auto-generated method stub
		return "The /1/jiracomment API helps you to manage your jira documents.";
	}


	/**
	 * Get a document of type JIRA_ISSUE_COMMENT_TYPE with its coordinates (index, type, id)
	 * @param index
	 * @param type
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{index}/{type}/{id}")
	public @ResponseBody
	JiraIssueComment get(@PathVariable String index, @PathVariable String type,
			@PathVariable String id) {
		return jiraIssueCommentService.get(index, type, id);
	}

	/**
	 * Get a document of type JIRA_ISSUE_COMMENT_TYPE in a given index knowing its id
	 * @param index
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{index}/{id}")
	public @ResponseBody
	JiraIssueComment get(@PathVariable String index, @PathVariable String id) {
		return get(index, JIRA_ISSUE_COMMENT_TYPE, id);
	}

	/**
	 * Get a document of index/type (JIRA_COMPONENT_INDEX/JIRA_ISSUE_COMMENT_TYPE) knowing its id
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public @ResponseBody
	JiraIssueComment get(@PathVariable String id) {
		return get(JIRA_COMPONENT_INDEX, JIRA_ISSUE_COMMENT_TYPE, id);
	}
	

}
