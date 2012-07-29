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

package org.scrutmydocs.webapp.api.settings.rivers.basic.facade;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.webapp.api.common.RestAPIException;
import org.scrutmydocs.webapp.api.common.data.Api;
import org.scrutmydocs.webapp.api.common.facade.CommonBaseApi;
import org.scrutmydocs.webapp.api.settings.rivers.basic.data.BasicRiver;
import org.scrutmydocs.webapp.api.settings.rivers.basic.data.RestResponseRivers;
import org.scrutmydocs.webapp.service.settings.rivers.RiverService;
import org.scrutmydocs.webapp.service.settings.rivers.basic.AdminRiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/1/settings/rivers")
public class RiversApi extends CommonBaseApi {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired	protected AdminRiverService adminService;
	@Autowired	protected RiverService riverService;


	@Override
	public Api[] helpApiList() {
		Api[] apis = new Api[1];
		apis[0] = new Api("/1/settings/rivers", "GET", "Get all existing rivers");
		return apis;
	}
	
	@Override
	public String helpMessage() {
		return "The /1/settings/rivers API gives details about rivers.";
	}
	
	/**
	 * Search for rivers
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody RestResponseRivers get() throws Exception {
		List<BasicRiver> fsrivers = null;
		try {
			fsrivers = adminService.get();
			
			// For each river, we must look if it's running or not
			for (BasicRiver fsRiver : fsrivers) {
				fsRiver.setStart(riverService.checkState(fsRiver));
			}
			
		} catch (Exception e) {
			return new RestResponseRivers(new RestAPIException(e));
		}
		
		return new RestResponseRivers(fsrivers);
	}
}
