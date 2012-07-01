/*
 * Licensed to David Pilato and Malloum Laya (the "Authors") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Authors licenses this
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

package fr.issamax.itest.essearch.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import fr.issamax.essearch.api.common.data.RestResponseWelcome;
import fr.issamax.essearch.api.common.data.Welcome;

public class WelcomeApiTest extends AbstractConfigurationIntegrationTest {
	private static final String BASE_URL = "http://localhost:9090/essearch/api/_help";

	private ESLogger logger = ESLoggerFactory.getLogger(WelcomeApiTest.class
			.getName());

	@Autowired private RestTemplate restTemplate;

	@Test
	public void asking_for_help() throws Exception {
		RestResponseWelcome response = restTemplate.getForObject(BASE_URL, RestResponseWelcome.class);
		assertNotNull(response);
		assertTrue(response.isOk());
		assertNotNull(response.getObject());
		Welcome output = (Welcome) response.getObject();
		assertNotNull(output);
		assertNotNull(output.getApis());
		assertTrue(output.getApis().length > 0);
	}

}
