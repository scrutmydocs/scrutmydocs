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

package org.scrutmydocs.webapp.api.settings.rivers.fsriver.facade;

import org.scrutmydocs.webapp.api.common.data.Api;
import org.scrutmydocs.webapp.api.common.facade.CommonBaseApi;
import org.scrutmydocs.webapp.api.settings.rivers.fsriver.data.FSRiver;
import org.scrutmydocs.webapp.api.settings.rivers.fsriver.data.RestResponseFSRiver;
import org.scrutmydocs.webapp.api.settings.rivers.fsriver.data.RestResponseFSRivers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/settings/rivers/fsriver")
@Deprecated
public class DeprecatedFSRiversApi extends CommonBaseApi {

	@Autowired protected FSRiversApi fsRiverApi;
	

	@Override
	public Api[] helpApiList() {
		return fsRiverApi.helpApiList();
	}
	
	@Override
	public String helpMessage() {
		return fsRiverApi.helpMessage();
	}
	
	/**
	 * Search for all FS rivers
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody RestResponseFSRivers get() throws Exception {
		return fsRiverApi.get();
	}
	
	/**
	 * Search for one FS river
	 * @return
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public @ResponseBody RestResponseFSRiver get(@PathVariable final String id) throws Exception {
		return fsRiverApi.get(id);
	}

	/**
	 * Create or Update a FS river
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody RestResponseFSRiver put(@RequestBody FSRiver fsriver) throws Exception {
		return fsRiverApi.put(fsriver);
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
		return fsRiverApi.delete(id);
	}
	
	/**
	 * Start a river
	 * @return
	 */
	@RequestMapping(value = "{id}/start", method = RequestMethod.GET)
	public @ResponseBody RestResponseFSRiver start(@PathVariable final String id) throws Exception {
		return fsRiverApi.start(id);
	}

	/**
	 * Stop a river
	 * @return
	 */
	@RequestMapping(value = "{id}/stop", method = RequestMethod.GET)
	public @ResponseBody RestResponseFSRiver stop(@PathVariable final String id) throws Exception {
		return fsRiverApi.stop(id);
	}
}
