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
import org.scrutmydocs.webapp.api.common.data.RestResponse;
import org.scrutmydocs.webapp.api.settings.rivers.abstractfs.data.AbstractFSRiver;
import org.scrutmydocs.webapp.itest.api.AbstractApiTest;
import org.springframework.http.HttpEntity;


/**
 * Abstract Test for module "settings/rivers/T/" where T depends on implementation
 * @author David Pilato
 */
public abstract class AbstractFSRiversApiTest<T extends AbstractFSRiver, U extends RestResponse<T>, V extends RestResponse<List<T>>> extends AbstractApiTest {

	private List<String> _fsRivers = null;
	
	/**
	 * @return The type you want to test
	 */
	abstract protected String type();
	
	/**
	 * @return The RestResponse<T> class implementation for your type
	 */
	abstract protected Class<U> getClassForSingleResponse();
	
	/**
	 * @return The RestResponse<List<T>>> class implementation for your type
	 */
	abstract protected Class<V> getClassForMultipleResponse();
	
	/**
	 * Module is "settings/rivers/abstractfs/" where abstractfs
	 * is replaced with {@link #type()}
	 */
	@Override
	protected String getModuleApiUrl() {
		return "settings/rivers/" + type() + "/";
	}

	/**
	 * Build a river instance of your type
	 * @param name
	 */
	abstract protected T buildRiverInstance(String name); 
	
	@Test
	public void get_all() throws Exception {
		// Add 3 rivers
		setupAddRiver(buildRiverInstance("all_mydummy1"));
		setupAddRiver(buildRiverInstance("all_mydummy2"));
		setupAddRiver(buildRiverInstance("all_mydummy3"));
		
		
		String url = buildFullApiUrl();

		V output = restTemplate.getForObject(url, getClassForMultipleResponse());
		assertNotNull(output);
		assertTrue(output.isOk());

		// We should have at least 3 rivers
		List<T> fsRivers = (List<T>) output.getObject();
		assertNotNull(fsRivers);
		
		int riverMatches = 0;
		// We should find our 3 rivers
		for (T fsRiver : fsRivers) {
			if (fsRiver.getId().equals("all_mydummy1") || 
					fsRiver.getId().equals("all_mydummy2") ||
					fsRiver.getId().equals("all_mydummy3")) riverMatches++;
		}
		
		assertEquals(3, riverMatches);
	}

	@Test
	public void get_one() throws Exception {
		// Add one river
		T fsRiver = buildRiverInstance("one_mydummy1");
		setupAddRiver(fsRiver);
		
		String url = buildFullApiUrl("one_mydummy1");
		U output = restTemplate.getForObject(url, getClassForSingleResponse());
		assertNotNull(output);
		assertTrue(output.isOk());
		assertEquals(fsRiver.getId(), ((T)output.getObject()).getId());
		assertEquals(fsRiver.getUrl(), ((T)output.getObject()).getUrl());
		assertEquals(fsRiver.getUpdateRate(), ((T)output.getObject()).getUpdateRate());
	}

	@Test
	public void push_with_post() throws Exception {
		T fsRiver = buildRiverInstance("post_mydummyfs");
		
		// We just add the created river to be cleaned after test
		_fsRivers.add("post_mydummyfs");
		
		U response = restTemplate.postForObject(buildFullApiUrl(),
				fsRiver, getClassForSingleResponse(), new Object[] {});
		assertNotNull(response);
		assertTrue(response.isOk());
	}
	
	@Test
	public void push_with_put() throws Exception {
		T fsRiver = buildRiverInstance("put_mydummyfs");
		
		// We just add the created river to be cleaned after test
		_fsRivers.add("put_mydummyfs");
		
		HttpEntity<T> entity = new HttpEntity<T>(fsRiver);
		restTemplate.put(buildFullApiUrl(), entity);
	}

	
	@Test
	public void start_non_existing_river() throws Exception {
		String url = buildFullApiUrl("start_mydummy/start");
		U output = restTemplate.getForObject(url, getClassForSingleResponse());
		assertNotNull(output);
		assertFalse(output.isOk());
	}

	@Test
	public void stop_non_existing_river() throws Exception {
		String url = buildFullApiUrl("stop_mydummy/stop");
		U output = restTemplate.getForObject(url, getClassForSingleResponse());
		assertNotNull(output);
		assertFalse(output.isOk());
	}

	@Test
	public void start_and_stop_river() throws Exception {
		T fsRiver = buildRiverInstance("start_stop_mydummy");
		setupAddRiver(fsRiver);

		String url = buildFullApiUrl("start_stop_mydummy/start");
		U output = restTemplate.getForObject(url, getClassForSingleResponse());
		assertNotNull(output);
		assertTrue(output.isOk());

		url = buildFullApiUrl("start_stop_mydummy/stop");
		output = restTemplate.getForObject(url, getClassForSingleResponse());
		assertNotNull(output);
		assertTrue(output.isOk());
	}

	@Test
	public void check_running_status_river() throws Exception {
		T fsRiver = buildRiverInstance("start_stop_mydummy");
		setupAddRiver(fsRiver);

		String url = buildFullApiUrl("start_stop_mydummy/start");
		U output = restTemplate.getForObject(url, getClassForSingleResponse());
		assertNotNull(output);
		assertTrue(output.isOk());
		
		// We get now the river status
		url = buildFullApiUrl("start_stop_mydummy");
		output = restTemplate.getForObject(url, getClassForSingleResponse());
		assertNotNull(output);
		assertTrue(output.isOk());
		assertTrue(((T)output.getObject()).isStart());
		
		url = buildFullApiUrl("start_stop_mydummy/stop");
		output = restTemplate.getForObject(url, getClassForSingleResponse());
		assertNotNull(output);
		assertTrue(output.isOk());
	}

	@Test
	public void delete_running_river_should_stop_it() throws Exception {
		T fsRiver = buildRiverInstance("stop_running_mydummy");
		addRiver(fsRiver);

		String url = buildFullApiUrl("stop_running_mydummy/start");
		U output = restTemplate.getForObject(url, getClassForSingleResponse());
		assertNotNull(output);
		assertTrue(output.isOk());
		
		// We delete the river
		deleteRiver("stop_running_mydummy");
		
		// We get now the river status
		url = buildFullApiUrl("stop_running_mydummy");
		output = restTemplate.getForObject(url, getClassForSingleResponse());
		assertNotNull(output);
		assertTrue(output.isOk());
		
		// We can not really check that the river has been removed but we can
		// check at least that we don't get back a SMD administrative object
		assertNull(output.getObject());
	}


	// Utility methods
	/**
	 * Add a river
	 * @param fsRiver
	 */
	protected void addRiver(T fsRiver) {
		HttpEntity<T> entity = new HttpEntity<T>(fsRiver);
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
	protected void setupAddRiver(T fsriver) {
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
