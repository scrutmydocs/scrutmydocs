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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.junit.Test;
import org.scrutmydocs.webapp.api.common.data.RestResponseWelcome;
import org.scrutmydocs.webapp.api.common.data.Welcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;


/**
 * If you extend this test class, it will check
 * if your API respect some rules :
 * <ul>
 * <li>You should have an entry point GET /_help on your API
 * @author David Pilato
 *
 */
public abstract class AbstractApiTest extends AbstractConfigurationIntegrationTest {
	protected static final String BASE_URL_SERVER = "localhost";
	protected static final String BASE_URL_PORT = "9090";
	protected static final String BASE_URL_SUFFIX = "/scrutmydocs/api/";

	protected String hostname;
	protected String port;
	
	private ESLogger logger = ESLoggerFactory.getLogger(AbstractApiTest.class
			.getName());

	/**
	 * If you want to run test from your IDE:
	 * <ul>
	 * <li>Start ScrutMyDocs in your container
	 * <li>Define -Dscrutmydocs.host=localhost -Dscrutmydocs.port=9090 with your server address
	 * <li>By default, integration tests run on localhost:9090
	 * </ul>
	 */
	public AbstractApiTest() {
		// We check if we run tests outside maven integration tests
		hostname = System.getProperty("scrutmydocs.host", BASE_URL_SERVER);
		port = System.getProperty("scrutmydocs.port", BASE_URL_PORT);
	}
	
	/**
	 * Define your API URL to append to the common base, e.g.: doc/
	 * @return
	 */
	protected abstract String getModuleApiUrl();
	
	/**
	 * Rest Client is automatically injected
	 */
	@Autowired protected RestTemplate restTemplate;

	/**
	 * We check that your API respect the rule : you should provide some help
	 * @throws Exception
	 */
	@Test
	public void asking_for_help() throws Exception {
		logger.debug("Testing _help entry point for {} api.", getModuleApiUrl() == null ? "/" : getModuleApiUrl());
		RestResponseWelcome response = restTemplate.getForObject(buildFullApiUrl("_help"), RestResponseWelcome.class);
		assertNotNull(response);
		assertTrue(response.isOk());
		assertNotNull(response.getObject());
		Welcome output = (Welcome) response.getObject();
		assertNotNull(output);
		assertNotNull(output.getApis());
		assertTrue(output.getApis().length > 0);
		logger.debug("{} _help entry point provided {} hints.", 
				getModuleApiUrl() == null ? "/" : getModuleApiUrl(), 
				output.getApis().length);
	}

	/**
	 * Here you can get the final URL you want to test
	 * @param append String to append to URL (could be null)
	 * @return A full URL for test
	 */
	protected String buildFullApiUrl(String append) {
		StringBuffer sbf = new StringBuffer("http://");
		sbf.append(hostname);
		sbf.append(":");
		sbf.append(port);
		sbf.append(BASE_URL_SUFFIX);
		if (getModuleApiUrl() != null) sbf.append(getModuleApiUrl());
		if (append != null) sbf.append(append);
		return sbf.toString();
	}

	/**
	 * Here you can get the final URL you want to test
	 * @return A full URL for test
	 */
	protected String buildFullApiUrl() {
		return buildFullApiUrl(null);
	}
}
