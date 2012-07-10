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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


public abstract class CommonBaseApi {

	public abstract String helpMessage();
	
	public abstract Api[] helpApiList();
	
	@RequestMapping(method = RequestMethod.GET, value = "_help")
	public @ResponseBody
	RestResponseWelcome welcomeHelp() {
		// Build a Welcome Message
		Welcome welcome = new Welcome(helpMessage());
		welcome.setApis(helpApiList());
		return new RestResponseWelcome(welcome);
	}
}