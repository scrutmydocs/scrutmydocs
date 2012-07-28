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

package org.scrutmydocs.webapp.api.settings.rivers.fs.facade;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.webapp.api.common.RestAPIException;
import org.scrutmydocs.webapp.api.common.data.Api;
import org.scrutmydocs.webapp.api.common.facade.CommonBaseApi;
import org.scrutmydocs.webapp.api.settings.rivers.basic.data.BasicRiver;
import org.scrutmydocs.webapp.api.settings.rivers.fs.data.FSRiver;
import org.scrutmydocs.webapp.api.settings.rivers.fs.data.RestResponseFSRiver;
import org.scrutmydocs.webapp.api.settings.rivers.fs.data.RestResponseFSRivers;
import org.scrutmydocs.webapp.service.settings.rivers.RiverService;
import org.scrutmydocs.webapp.service.settings.rivers.fs.AdminFSRiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/settings/rivers/fs")
public class FSRiversApi extends CommonBaseApi {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired protected AdminFSRiverService adminService;
	@Autowired protected RiverService riverService;
	

	@Override
	public Api[] helpApiList() {
		Api[] apis = new Api[7];
		apis[0] = new Api("/settings/rivers/fs", "GET", "Get all existing FileSystem rivers");
		apis[1] = new Api("/settings/rivers/fs{name}", "GET", "Get details about a FileSystem river");
		apis[2] = new Api("/settings/rivers/fs", "PUT", "Create or update a FileSystem river");
		apis[3] = new Api("/settings/rivers/fs", "POST", "Create or update a FileSystem river");
		apis[4] = new Api("/settings/rivers/fs/{name}", "DELETE", "Delete an existing FileSystem river");
		apis[5] = new Api("/settings/rivers/fs/{name}/start", "GET", "Start a river");
		apis[6] = new Api("/settings/rivers/fs/{name}/stop", "GET", "Stop a river");
		return apis;
	}
	
	@Override
	public String helpMessage() {
		return "The /settings/rivers/fs API manage FileSystem rivers.";
	}
	
	/**
	 * Search for all FS rivers
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody RestResponseFSRivers get() throws Exception {
		List<FSRiver> fsrivers = null;
		try {
			fsrivers = adminService.get();
			
			// For each river, we must look if it's running or not
			for (BasicRiver fsRiver : fsrivers) {
				fsRiver.setStart(riverService.checkState(fsRiver));
			}
			
		} catch (Exception e) {
			return new RestResponseFSRivers(new RestAPIException(e));
		}
		
		return new RestResponseFSRivers(fsrivers);
	}
	
	/**
	 * Search for one FS river
	 * @return
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public @ResponseBody RestResponseFSRiver get(@PathVariable final String id) throws Exception {
		FSRiver fsriver = null;
		try {
			fsriver = adminService.get(id);
			if (fsriver != null) {
				fsriver.setStart(riverService.checkState(fsriver));
			}
		} catch (Exception e) {
			return new RestResponseFSRiver(new RestAPIException(e));
		}
		
		return new RestResponseFSRiver(fsriver);
	}

	/**
	 * Create or Update a FS river
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody RestResponseFSRiver put(@RequestBody FSRiver fsriver) throws Exception {
		try {
			adminService.update(fsriver);
		} catch (Exception e) {
			return new RestResponseFSRiver(new RestAPIException(e));
		}
		
		return new RestResponseFSRiver();
	}
	
	/**
	 * Create or Update a FS river
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody RestResponseFSRiver push(@RequestBody FSRiver fsriver) throws Exception {
		return put(fsriver);
	}

	/**
	 * Remove an FS river
	 * @return
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public @ResponseBody RestResponseFSRiver delete(@PathVariable final String id) throws Exception {
		try {
			adminService.remove(new FSRiver(id, null, null));
		} catch (Exception e) {
			return new RestResponseFSRiver(new RestAPIException(e));
		}
		
		return new RestResponseFSRiver();
	}
	
	/**
	 * Start a river
	 * @return
	 */
	@RequestMapping(value = "{id}/start", method = RequestMethod.GET)
	public @ResponseBody RestResponseFSRiver start(@PathVariable final String id) throws Exception {
		FSRiver fsriver = null;
		try {
			fsriver = adminService.get(id);
			if (fsriver == null) {
				return new RestResponseFSRiver(new RestAPIException("River " + id + " does not exist."));
			}
			fsriver.setStart(true);
			adminService.start(fsriver);
		} catch (Exception e) {
			return new RestResponseFSRiver(new RestAPIException(e));
		}
		
		return new RestResponseFSRiver();
	}

	/**
	 * Stop a river
	 * @return
	 */
	@RequestMapping(value = "{id}/stop", method = RequestMethod.GET)
	public @ResponseBody RestResponseFSRiver stop(@PathVariable final String id) throws Exception {
		FSRiver fsriver = null;
		try {
			fsriver = adminService.get(id);
			if (fsriver == null) {
				return new RestResponseFSRiver(new RestAPIException("River " + id + " does not exist."));
			}
			fsriver.setStart(false);
			riverService.stop(fsriver);
		} catch (Exception e) {
			return new RestResponseFSRiver(new RestAPIException(e));
		}
		
		return new RestResponseFSRiver();
	}


}
