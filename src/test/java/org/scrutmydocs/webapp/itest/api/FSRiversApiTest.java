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

package org.scrutmydocs.webapp.itest.api;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scrutmydocs.webapp.api.settings.rivers.fsriver.data.FSRiver;
import org.scrutmydocs.webapp.api.settings.rivers.fsriver.data.RestResponseFSRiver;
import org.scrutmydocs.webapp.api.settings.rivers.fsriver.data.RestResponseFSRivers;
import org.springframework.http.HttpEntity;


/**
 * Test for module "settings/rivers/fsriver/"
 * @author David Pilato
 */
public class FSRiversApiTest extends AbstractApiTest {
	
	/**
	 * Module is "settings/rivers/fsriver/"
	 */
	@Override
	protected String getModuleApiUrl() {
		return "settings/rivers/fsriver/";
	}

	@Test
	public void getAllRivers() throws Exception {
		String url = buildFullApiUrl();

		RestResponseFSRivers output = restTemplate.getForObject(url, RestResponseFSRivers.class);
		assertNotNull(output);
	}

	@Test
	public void push_get_delete_river() throws Exception {
		FSRiver fsRiver = new FSRiver();
		HttpEntity<FSRiver> entity = new HttpEntity<FSRiver>(fsRiver);
		restTemplate.put(buildFullApiUrl(), entity);

		String url = buildFullApiUrl("TODO");
		RestResponseFSRiver output = restTemplate.getForObject(url, RestResponseFSRiver.class);
		assertNotNull(output);
		
		restTemplate.delete(url);
	}

	@Test
	public void push_with_post_get_delete_river() throws Exception {
		FSRiver fsRiver = new FSRiver();
		fsRiver.setId("mydummyfs");
		RestResponseFSRiver response = restTemplate.postForObject(buildFullApiUrl(),
				fsRiver, RestResponseFSRiver.class, new Object[] {});
		assertNotNull(response);
		assertTrue(response.isOk());
		
		String url = buildFullApiUrl("mydummyfs");
		RestResponseFSRiver output = restTemplate.getForObject(url, RestResponseFSRiver.class);
		assertNotNull(output);
		assertTrue(output.isOk());
		assertEquals(fsRiver.getId(), ((FSRiver)output.getObject()).getId());
		assertEquals(fsRiver.getUrl(), ((FSRiver)output.getObject()).getUrl());
		assertEquals(fsRiver.getUpdateRate(), ((FSRiver)output.getObject()).getUpdateRate());

		restTemplate.delete(url);
	}

}
