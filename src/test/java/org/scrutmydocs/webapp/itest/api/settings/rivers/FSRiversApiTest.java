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

package org.scrutmydocs.webapp.itest.api.settings.rivers;

import org.scrutmydocs.webapp.api.settings.rivers.fs.data.FSRiver;
import org.scrutmydocs.webapp.api.settings.rivers.fs.data.RestResponseFSRiver;
import org.scrutmydocs.webapp.api.settings.rivers.fs.data.RestResponseFSRivers;


/**
 * Test for module "1/settings/rivers/fsriver/"
 * @author David Pilato
 */
public class FSRiversApiTest extends AbstractFSRiversApiTest<FSRiver, RestResponseFSRiver, RestResponseFSRivers> {
	
	@Override
	protected String apiVersion() {
		return "1";
	}

	@Override
	protected String type() {
		return "fs";
	}

	@Override
	protected FSRiver buildRiverInstance(String name) {
		return new FSRiver(name, "/thisdirshouldnotexist", 30L);
	}

	@Override
	protected Class<RestResponseFSRiver> getClassForSingleResponse() {
		return RestResponseFSRiver.class;
	}

	@Override
	protected Class<RestResponseFSRivers> getClassForMultipleResponse() {
		return RestResponseFSRivers.class;
	}
}
