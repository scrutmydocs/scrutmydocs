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

package org.scrutmydocs.webapp.api.settings.rivers.dropbox.facade;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.webapp.api.common.RestAPIException;
import org.scrutmydocs.webapp.api.common.data.Api;
import org.scrutmydocs.webapp.api.common.facade.CommonBaseApi;
import org.scrutmydocs.webapp.api.settings.rivers.basic.data.BasicRiver;
import org.scrutmydocs.webapp.api.settings.rivers.dropbox.data.DropBoxRiver;
import org.scrutmydocs.webapp.api.settings.rivers.dropbox.data.RestResponseDropBoxRiver;
import org.scrutmydocs.webapp.api.settings.rivers.dropbox.data.RestResponseDropBoxRivers;
import org.scrutmydocs.webapp.service.settings.rivers.RiverService;
import org.scrutmydocs.webapp.service.settings.rivers.dropbox.AdminDropBoxRiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/1/settings/rivers/dropbox")
public class DropBoxRiversApi extends CommonBaseApi {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired protected AdminDropBoxRiverService adminService;
	@Autowired protected RiverService riverService;
	

	@Override
	public Api[] helpApiList() {
		Api[] apis = new Api[7];
		apis[0] = new Api("/1/settings/rivers/dropbox", "GET", "Get all existing FileSystem rivers");
		apis[1] = new Api("/1/settings/rivers/dropbox/{name}", "GET", "Get details about a FileSystem river");
		apis[2] = new Api("/1/settings/rivers/dropbox", "PUT", "Create or update a FileSystem river");
		apis[3] = new Api("/1/settings/rivers/dropbox", "POST", "Create or update a FileSystem river");
		apis[4] = new Api("/1/settings/rivers/dropbox/{name}", "DELETE", "Delete an existing FileSystem river");
		apis[5] = new Api("/1/settings/rivers/dropbox/{name}/start", "GET", "Start a river");
		apis[6] = new Api("/1/settings/rivers/dropbox/{name}/stop", "GET", "Stop a river");
		return apis;
	}
	
	@Override
	public String helpMessage() {
		return "The /1/settings/rivers/dropbox API manage Dropbox rivers.";
	}
	
	/**
	 * Search for all Dropbox rivers
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody RestResponseDropBoxRivers get() throws Exception {
		List<DropBoxRiver> rivers = null;
		try {
			rivers = adminService.get();
			
			// For each river, we must look if it's running or not
			for (BasicRiver river : rivers) {
				river.setStart(riverService.checkState(river));
			}
			
		} catch (Exception e) {
			return new RestResponseDropBoxRivers(new RestAPIException(e));
		}
		
		return new RestResponseDropBoxRivers(rivers);
	}
	
	/**
	 * Search for one Dropbox river
	 * @return
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public @ResponseBody RestResponseDropBoxRiver get(@PathVariable final String id) throws Exception {
		DropBoxRiver river = null;
		try {
			river = adminService.get(id);
			if (river != null) {
				river.setStart(riverService.checkState(river));
			}
		} catch (Exception e) {
			return new RestResponseDropBoxRiver(new RestAPIException(e));
		}
		
		return new RestResponseDropBoxRiver(river);
	}

	/**
	 * Create or Update a Dropbox river
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody RestResponseDropBoxRiver put(@RequestBody DropBoxRiver river) throws Exception {
		try {
			adminService.update(river);
		} catch (Exception e) {
			return new RestResponseDropBoxRiver(new RestAPIException(e));
		}
		
		return new RestResponseDropBoxRiver();
	}
	
	/**
	 * Create or Update a Dropbox river
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody RestResponseDropBoxRiver push(@RequestBody DropBoxRiver river) throws Exception {
		return put(river);
	}

	/**
	 * Remove a Dropbox river
	 * @return
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public @ResponseBody RestResponseDropBoxRiver delete(@PathVariable final String id) throws Exception {
		try {
			adminService.remove(new DropBoxRiver(id, null, null, null, null));
		} catch (Exception e) {
			return new RestResponseDropBoxRiver(new RestAPIException(e));
		}
		
		return new RestResponseDropBoxRiver();
	}
	
	/**
	 * Start a river
	 * @return
	 */
	@RequestMapping(value = "{id}/start", method = RequestMethod.GET)
	public @ResponseBody RestResponseDropBoxRiver start(@PathVariable final String id) throws Exception {
		DropBoxRiver river = null;
		try {
			river = adminService.get(id);
			if (river == null) {
				return new RestResponseDropBoxRiver(new RestAPIException("River " + id + " does not exist."));
			}
			river.setStart(true);
			adminService.start(river);
		} catch (Exception e) {
			return new RestResponseDropBoxRiver(new RestAPIException(e));
		}
		
		return new RestResponseDropBoxRiver();
	}

	/**
	 * Stop a river
	 * @return
	 */
	@RequestMapping(value = "{id}/stop", method = RequestMethod.GET)
	public @ResponseBody RestResponseDropBoxRiver stop(@PathVariable final String id) throws Exception {
		BasicRiver river = null;
		try {
			river = adminService.get(id);
			if (river == null) {
				return new RestResponseDropBoxRiver(new RestAPIException("River " + id + " does not exist."));
			}
			river.setStart(false);
			riverService.stop(river);
		} catch (Exception e) {
			return new RestResponseDropBoxRiver(new RestAPIException(e));
		}
		
		return new RestResponseDropBoxRiver();
	}


}
