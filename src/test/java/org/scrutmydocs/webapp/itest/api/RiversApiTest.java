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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scrutmydocs.webapp.api.settings.rivers.data.AbstractRiver;
import org.scrutmydocs.webapp.api.settings.rivers.data.RestResponseRivers;
import org.scrutmydocs.webapp.api.settings.rivers.fsriver.data.FSRiver;
import org.springframework.http.HttpEntity;


/**
 * Test for module "settings/rivers/"
 * @author David Pilato
 */
public class RiversApiTest extends AbstractApiTest {

	private List<String> _fsRivers = null;
	
	/**
	 * Module is "settings/rivers/"
	 */
	@Override
	protected String getModuleApiUrl() {
		return "settings/rivers/";
	}

	@Test
	public void get_all() throws Exception {
		// Add 3 rivers
		setupAddRiver(new FSRiver("all_mydummy1", "/dummydir1", 30L));
		setupAddRiver(new FSRiver("all_mydummy2", "/dummydir2", 60L));
		setupAddRiver(new FSRiver("all_mydummy3", "/dummydir3", 90L));
		
		
		String url = buildFullApiUrl();

		RestResponseRivers output = restTemplate.getForObject(url, RestResponseRivers.class);
		assertNotNull(output);
		assertTrue(output.isOk());

		// We should have at least 3 rivers
		List<AbstractRiver> rivers = (List<AbstractRiver>) output.getObject();
		assertNotNull(rivers);
		
		int riverMatches = 0;
		// We should find our 3 rivers
		for (AbstractRiver river : rivers) {
			if (river.getId().equals("all_mydummy1") || 
					river.getId().equals("all_mydummy2") ||
					river.getId().equals("all_mydummy3")) riverMatches++;
		}
		
		assertEquals(3, riverMatches);
	}

	// Utility methods
	/**
	 * Add a river
	 * @param fsriver
	 */
	protected void addRiver(FSRiver fsriver) {
		HttpEntity<FSRiver> entity = new HttpEntity<FSRiver>(fsriver);
		restTemplate.put(buildFullApiUrl("fsrivers/"), entity);
	}

	/**
	 * Clean a test case : Delete a river
	 * @param id River Id
	 */
	protected void deleteRiver(String id) {
		restTemplate.delete(buildFullApiUrl("fsrivers/" + id));
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
