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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
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

	private List<String> _fsRivers = null;
	
	/**
	 * Module is "settings/rivers/fsriver/"
	 */
	@Override
	protected String getModuleApiUrl() {
		return "settings/rivers/fsriver/";
	}

	@Test
	public void get_all() throws Exception {
		// Add 3 rivers
		setupAddRiver(new FSRiver("all_mydummy1", "/dummydir1", 30L));
		setupAddRiver(new FSRiver("all_mydummy2", "/dummydir2", 60L));
		setupAddRiver(new FSRiver("all_mydummy3", "/dummydir3", 90L));
		
		
		String url = buildFullApiUrl();

		RestResponseFSRivers output = restTemplate.getForObject(url, RestResponseFSRivers.class);
		assertNotNull(output);
		assertTrue(output.isOk());

		// We should have at least 3 rivers
		List<FSRiver> fsRivers = (List<FSRiver>) output.getObject();
		assertNotNull(fsRivers);
		
		int riverMatches = 0;
		// We should find our 3 rivers
		for (FSRiver fsRiver : fsRivers) {
			if (fsRiver.getId().equals("all_mydummy1") || 
					fsRiver.getId().equals("all_mydummy2") ||
					fsRiver.getId().equals("all_mydummy3")) riverMatches++;
		}
		
		assertEquals(3, riverMatches);
	}

	@Test
	public void get_one() throws Exception {
		// Add one river
		FSRiver fsRiver = new FSRiver("one_mydummy1", "/dummydir1", 30L);
		setupAddRiver(fsRiver);
		
		String url = buildFullApiUrl("one_mydummy1");
		RestResponseFSRiver output = restTemplate.getForObject(url, RestResponseFSRiver.class);
		assertNotNull(output);
		assertTrue(output.isOk());
		assertEquals(fsRiver.getId(), ((FSRiver)output.getObject()).getId());
		assertEquals(fsRiver.getUrl(), ((FSRiver)output.getObject()).getUrl());
		assertEquals(fsRiver.getUpdateRate(), ((FSRiver)output.getObject()).getUpdateRate());
	}

	@Test
	public void push_with_post() throws Exception {
		FSRiver fsRiver = new FSRiver();
		fsRiver.setId("post_mydummyfs");
		
		// We just add the created river to be cleaned after test
		_fsRivers.add("post_mydummyfs");
		
		RestResponseFSRiver response = restTemplate.postForObject(buildFullApiUrl(),
				fsRiver, RestResponseFSRiver.class, new Object[] {});
		assertNotNull(response);
		assertTrue(response.isOk());
	}
	
	@Test
	public void push_with_put() throws Exception {
		FSRiver fsRiver = new FSRiver();
		fsRiver.setId("put_mydummyfs");
		
		// We just add the created river to be cleaned after test
		_fsRivers.add("put_mydummyfs");
		
		HttpEntity<FSRiver> entity = new HttpEntity<FSRiver>(fsRiver);
		restTemplate.put(buildFullApiUrl(), entity);
	}

	
	@Test
	public void start_non_existing_river() throws Exception {
		String url = buildFullApiUrl("start_mydummy/start");
		RestResponseFSRiver output = restTemplate.getForObject(url, RestResponseFSRiver.class);
		assertNotNull(output);
		assertFalse(output.isOk());
	}

	@Test
	public void stop_non_existing_river() throws Exception {
		String url = buildFullApiUrl("stop_mydummy/stop");
		RestResponseFSRiver output = restTemplate.getForObject(url, RestResponseFSRiver.class);
		assertNotNull(output);
		assertFalse(output.isOk());
	}

	@Test
	public void start_and_stop_river() throws Exception {
		FSRiver fsRiver = new FSRiver("start_stop_mydummy", "/thisdirshouldnotexist", 30L);
		setupAddRiver(fsRiver);

		String url = buildFullApiUrl("start_stop_mydummy/start");
		RestResponseFSRiver output = restTemplate.getForObject(url, RestResponseFSRiver.class);
		assertNotNull(output);
		assertTrue(output.isOk());

		url = buildFullApiUrl("start_stop_mydummy/stop");
		output = restTemplate.getForObject(url, RestResponseFSRiver.class);
		assertNotNull(output);
		assertTrue(output.isOk());
	}

	@Test
	public void check_running_status_river() throws Exception {
		FSRiver fsRiver = new FSRiver("start_stop_mydummy", "/thisdirshouldnotexist", 30L);
		setupAddRiver(fsRiver);

		String url = buildFullApiUrl("start_stop_mydummy/start");
		RestResponseFSRiver output = restTemplate.getForObject(url, RestResponseFSRiver.class);
		assertNotNull(output);
		assertTrue(output.isOk());
		
		// We get now the river status
		url = buildFullApiUrl("start_stop_mydummy");
		output = restTemplate.getForObject(url, RestResponseFSRiver.class);
		assertNotNull(output);
		assertTrue(output.isOk());
		assertTrue(((FSRiver)output.getObject()).isStart());
		
		url = buildFullApiUrl("start_stop_mydummy/stop");
		output = restTemplate.getForObject(url, RestResponseFSRiver.class);
		assertNotNull(output);
		assertTrue(output.isOk());
	}

	@Test
	public void delete_running_river_should_stop_it() throws Exception {
		FSRiver fsRiver = new FSRiver("stop_running_mydummy", "/thisdirshouldnotexist", 30L);
		addRiver(fsRiver);

		String url = buildFullApiUrl("stop_running_mydummy/start");
		RestResponseFSRiver output = restTemplate.getForObject(url, RestResponseFSRiver.class);
		assertNotNull(output);
		assertTrue(output.isOk());
		
		// We delete the river
		deleteRiver("stop_running_mydummy");
		
		// We get now the river status
		url = buildFullApiUrl("stop_running_mydummy");
		output = restTemplate.getForObject(url, RestResponseFSRiver.class);
		assertNotNull(output);
		assertTrue(output.isOk());
		
		// We can not really check that the river has been removed but we can
		// check at least that we don't get back a SMD administrative object
		assertNull(output.getObject());
	}


	// Utility methods
	/**
	 * Add a river
	 * @param fsriver
	 */
	protected void addRiver(FSRiver fsriver) {
		HttpEntity<FSRiver> entity = new HttpEntity<FSRiver>(fsriver);
		restTemplate.put(buildFullApiUrl(), entity);
	}

	/**
	 * Clean a test case : Delete a river
	 * @param id River Id
	 */
	protected void deleteRiver(String id) {
		restTemplate.delete(buildFullApiUrl(id));
	}

	// Setup Methods
	/**
	 * Prepare a test case
	 */
	@Before
	public void startTest() {
		_fsRivers = new ArrayList<String>();
	}

	/**
	 * Prepare a test case : Add a river and add it to the rivers to be released at end
	 * @param fsriver
	 */
	protected void setupAddRiver(FSRiver fsriver) {
		addRiver(fsriver);
		_fsRivers.add(fsriver.getId());
	}

	/**
	 * Clean after test case
	 */
	@After
	public void stopTest() {
		if (_fsRivers != null) {
			for (String fsRiver : _fsRivers) {
				deleteRiver(fsRiver);
			}
		}
	}

}
