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

package org.scrutmydocs.webapp.api.settings.rivers.jira.facade;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.webapp.api.common.RestAPIException;
import org.scrutmydocs.webapp.api.common.data.Api;
import org.scrutmydocs.webapp.api.common.facade.CommonBaseApi;
import org.scrutmydocs.webapp.api.settings.rivers.jira.data.JiraRiver;
import org.scrutmydocs.webapp.api.settings.rivers.jira.data.RestResponseJiraRiver;
import org.scrutmydocs.webapp.api.settings.rivers.jira.data.RestResponseJiraRivers;
import org.scrutmydocs.webapp.service.settings.rivers.RiverService;
import org.scrutmydocs.webapp.service.settings.rivers.jira.AdminJiraRiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/1/settings/rivers/jira")
public class JiraRiversApi extends CommonBaseApi {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired protected AdminJiraRiverService adminService;
	@Autowired protected RiverService riverService;
	

	@Override
	public Api[] helpApiList() {
		Api[] apis = new Api[7];
		apis[0] = new Api("/1/settings/rivers/jira", "GET", "Get all existing Jira rivers");
		apis[1] = new Api("/1/settings/rivers/jira/{name}", "GET", "Get details about a Jira river");
		apis[2] = new Api("/1/settings/rivers/jira", "PUT", "Create or update a Jira river");
		apis[3] = new Api("/1/settings/rivers/jira", "POST", "Create or update a Jira river");
		apis[4] = new Api("/1/settings/rivers/jira/{name}", "DELETE", "Delete an existing Jira river");
		apis[5] = new Api("/1/settings/rivers/jira/{name}/start", "GET", "Start a Jira river");
		apis[6] = new Api("/1/settings/rivers/jira/{name}/stop", "GET", "Stop a Jira river");
		return apis;
	}
	
	@Override
	public String helpMessage() {
		return "The /1/settings/rivers/jira API manage Jira rivers.";
	}
	
	/**
	 * Search for all Jira rivers
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody RestResponseJiraRivers get() throws Exception {
		List<JiraRiver> jirarivers = null;
		try {
			jirarivers = adminService.get();
			
			// For each river, we must look if it's running or not
			for (JiraRiver jiraRiver : jirarivers) {
				jiraRiver.setStart(riverService.checkState(jiraRiver));
			}
			
		} catch (Exception e) {
			return new RestResponseJiraRivers(new RestAPIException(e));
		}
		
		return new RestResponseJiraRivers(jirarivers);
	}
	
	/**
	 * Search for one Jira river
	 * @return
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public @ResponseBody RestResponseJiraRiver get(@PathVariable final String id) throws Exception {
		JiraRiver jirariver = null;
		try {
			jirariver = adminService.get(id);
			if (jirariver != null) {
				jirariver.setStart(riverService.checkState(jirariver));
			}
		} catch (Exception e) {
			return new RestResponseJiraRiver(new RestAPIException(e));
		}
		
		return new RestResponseJiraRiver(jirariver);
	}

	/**
	 * Create or Update a Jira river
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody RestResponseJiraRiver put(@RequestBody JiraRiver jirariver) throws Exception {
		try {
			adminService.update(jirariver);
		} catch (Exception e) {
			return new RestResponseJiraRiver(new RestAPIException(e));
		}
		
		return new RestResponseJiraRiver();
	}
	
	/**
	 * Create or Update a Jira river
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody RestResponseJiraRiver push(@RequestBody JiraRiver jirariver) throws Exception {
		return put(jirariver);
	}

	/**
	 * Remove an Jira river
	 * @return
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public @ResponseBody RestResponseJiraRiver delete(@PathVariable final String id) throws Exception {
        JiraRiver jirariver;
        try {
            jirariver = adminService.get(id);
            if (jirariver != null) {
                adminService.remove(jirariver);
            }
        } catch (Exception e) {
            return new RestResponseJiraRiver(new RestAPIException(e));
        }
		return new RestResponseJiraRiver();
	}
	
	/**
	 * Start a Jira river
	 * @return
	 */
	@RequestMapping(value = "{id}/start", method = RequestMethod.GET)
	public @ResponseBody RestResponseJiraRiver start(@PathVariable final String id) throws Exception {
		JiraRiver jirariver = null;
		try {
			jirariver = adminService.get(id);
			if (jirariver == null) {
				return new RestResponseJiraRiver(new RestAPIException("River " + id + " does not exist."));
			}
			jirariver.setStart(true);
			adminService.start(jirariver);
		} catch (Exception e) {
			return new RestResponseJiraRiver(new RestAPIException(e));
		}
		
		return new RestResponseJiraRiver();
	}

	/**
	 * Stop a Jira river
	 * @return
	 */
	@RequestMapping(value = "{id}/stop", method = RequestMethod.GET)
	public @ResponseBody RestResponseJiraRiver stop(@PathVariable final String id) throws Exception {
		JiraRiver jirariver = null;
		try {
			jirariver = adminService.get(id);
			if (jirariver == null) {
				return new RestResponseJiraRiver(new RestAPIException("River " + id + " does not exist."));
			}
			jirariver.setStart(false);
			riverService.stop(jirariver);
		} catch (Exception e) {
			return new RestResponseJiraRiver(new RestAPIException(e));
		}
		
		return new RestResponseJiraRiver();
	}


}
