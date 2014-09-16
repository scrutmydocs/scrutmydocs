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

package org.scrutmydocs.webapp.api.common.facade;

import org.scrutmydocs.webapp.api.common.data.Api;
import org.scrutmydocs.webapp.api.common.data.RestResponseWelcome;
import org.scrutmydocs.webapp.api.common.data.Welcome;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/")
public class WelcomeApi extends CommonBaseApi {

	@Override
	public Api[] helpApiList() {
		Api[] apis = new Api[7];
		apis[0] = new Api("/", "GET", "This API");
		apis[1] = new Api("_help", "GET", "Should give you help on each APIs.");
		apis[2] = new Api("/1/doc", "POST/GET/DELETE", "Manage documents.");
		apis[3] = new Api("/1/index", "POST/DELETE", "Manage Indices");
		apis[4] = new Api("/1/search", "POST", "Search for documents.");
		apis[5] = new Api("/1/settings/rivers", "GET", "Manage Rivers");
		apis[6] = new Api("/1/settings/rivers/fs", "GET/POST/DELETE", "Manage FileSystem rivers");
		return apis;
	}
	
	@Override
	public String helpMessage() {
		return "You are looking for help ??? ;-)";
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	RestResponseWelcome welcome() {
		return new RestResponseWelcome(new Welcome("Welcome to ScrutMyDocs RESTFul Service..."));
	}
}
