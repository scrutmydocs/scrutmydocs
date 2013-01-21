package org.scrutmydocs.webapp.api.jira.facade;

import org.scrutmydocs.webapp.api.common.RestAPIException;
import org.scrutmydocs.webapp.api.common.data.Api;
import org.scrutmydocs.webapp.api.common.facade.CommonBaseApi;
import org.scrutmydocs.webapp.api.jira.data.JiraIssue;
import org.scrutmydocs.webapp.api.jira.data.RestResponseJiraIssue;
import org.scrutmydocs.webapp.service.jira.JiraIssueService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import static org.scrutmydocs.webapp.constant.SMDSearchProperties.*;

/**
 * JiraIssue RESTFul API
 * <ul>
 * <li>PUT : create a new index to hold issue
 * <li>DELETE : remove an index
 * </ul>
 * @author Johann NG SING KWONG
 *
 */
@Controller
@RequestMapping("/1/jiraissue")
public class JiraIssueApi extends CommonBaseApi {
	
	@Autowired
	JiraIssueService jiraIssueService;
	
	@Override
	public Api[] helpApiList() {
		Api[] apis = new Api[7];
		//apis[0] = new Api("/1/jiraissue", "POST", "Add a jira document to the search engine");
		apis[1] = new Api("/1/jiraissue/{id}", "DELETE", "Delete a jira document in the default index/type (my_jira_index/jira_issue)");
		apis[2] = new Api("/1/jiraissue/{index}/{id}", "DELETE", "Delete a jira issue in the type (jira_issue)");
		apis[3] = new Api("/1/jiraissue/{index}/{type}/{id}", "DELETE", "Delete a jira document ");
		apis[4] = new Api("/1/jiraissue/{id}", "GET", "Get a jira issue  in the index/type (jira/my_jira_index/)");
		apis[5] = new Api("/1/jiraissue/{index}/{id}", "GET", "Get a jira issue in a specific index with default type (jira_issue)");
		apis[6] = new Api("/1/jiraissue/{index}/{type}/{id}", "GET", "Get a jira issue in a specific index/type");
		return apis;
	}

	@Override
	public String helpMessage() {
		// TODO Auto-generated method stub
		return "The /1/jiraissue API helps you to manage your jira issue documents.";
	}

	/**
	 * Add a new Jira document
     * TODO Can't be used for the moment. The method push is not implemented
	 * @param
	 * @return
	 */
//	@RequestMapping(method = RequestMethod.POST)
//	public @ResponseBody
//	RestResponseJiraIssue push(@RequestBody  JiraIssue jiraissue) {
//		try {
//			jiraissue = jiraIssueService.push(jiraissue);
//		} catch (RestAPIException e) {
//			return new RestResponseJiraIssue(e);
//		}
//		RestResponseJiraIssue response = new RestResponseJiraIssue(jiraissue);
//		return response;
//	}
	
	/**
	 * Get a document of type JIRA_ISSUE_TYPE with its coordinates (index, type, id)
	 * @param index
	 * @param type
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{index}/{type}/{id}")
	public @ResponseBody
	JiraIssue get(@PathVariable String index, @PathVariable String type,
			@PathVariable String id) {
		return jiraIssueService.get(index, type, id);
	}

	/**
	 * Get a document of type JIRA_ISSUE_TYPE in a given index knowing its id
	 * @param index
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{index}/{id}")
	public @ResponseBody
	JiraIssue get(@PathVariable String index, @PathVariable String id) {
		return get(index, JIRA_ISSUE_TYPE, id);
	}

	/**
	 * Get a document of index/type (JIRA_COMPONENT_INDEX/JIRA_ISSUE_TYPE) knowing its id
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public @ResponseBody
	JiraIssue get(@PathVariable String id) {
		return get(JIRA_COMPONENT_INDEX, JIRA_ISSUE_TYPE, id);
	}
	

}
