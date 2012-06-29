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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import fr.issamax.essearch.api.index.data.Index;
import fr.issamax.essearch.api.index.data.RestResponseIndex;
import fr.issamax.essearch.constant.ESSearchProperties;

public class IndexApiTest extends AbstractConfigurationIntegrationTest {
	private static final String BASE_URL = "http://localhost:9090/essearch/api/index/";

	private ESLogger logger = ESLoggerFactory.getLogger(IndexApiTest.class
			.getName());

	@Autowired
	private RestTemplate restTemplate;

	@Test
	public void create_index_with_defaults() throws Exception {
		RestResponseIndex response = restTemplate.postForObject(BASE_URL, new Index(),
				RestResponseIndex.class, new Object[] {});
		assertNotNull(response);
		assertTrue(response.isOk());
		assertNotNull(response.getObject());
		Index output = (Index) response.getObject();
		assertNotNull(output);
		assertEquals(ESSearchProperties.INDEX_NAME, output.getIndex());
		assertEquals(ESSearchProperties.INDEX_TYPE_DOC, output.getType());
		assertNull(output.getAnalyzer());
	}

	@Test
	public void create_index_with_values_in_object() throws Exception {
		String index = "myindex";
		String type = "mytype";
		String analyzer = "myanalyzer";
		Index input = new Index(index, type, analyzer);

		RestResponseIndex response = restTemplate.postForObject(BASE_URL, input,
				RestResponseIndex.class, new Object[] {});
		assertNotNull(response);
		assertTrue(response.isOk());
		assertNotNull(response.getObject());
		Index output = (Index) response.getObject();
		assertNotNull(output);
		assertEquals(index, output.getIndex());
		assertEquals(type, output.getType());
		assertEquals(analyzer, output.getAnalyzer());
		
	}

	@Test
	public void create_index_with_values_in_url() throws Exception {
		String index = "mysecondindex";
		String type = "mysecondtype";

		String url = BASE_URL + index + "/" + type;
		RestResponseIndex response = restTemplate.postForObject(url, new Index(),
				RestResponseIndex.class, new Object[] {});
		assertNotNull(response);
		assertTrue(response.isOk());
		assertNotNull(response.getObject());
		Index output = (Index) response.getObject();
		assertNotNull(output);
		assertEquals(index, output.getIndex());
		assertEquals(type, output.getType());
		assertNull(output.getAnalyzer());
		
	}

	@Test
	public void create_index_with_values_in_url_and_settings() throws Exception {
		String index = "mysecondindex";
		String type = "mysecondtype";
		Index settings = new Index(null, null, "french");
		
		String url = BASE_URL + index + "/" + type;
		RestResponseIndex response = restTemplate.postForObject(url, settings,
				RestResponseIndex.class, new Object[] {});
		assertNotNull(response);
		assertTrue(response.isOk());
		assertNotNull(response.getObject());
		Index output = (Index) response.getObject();
		assertNotNull(output);
		assertEquals(index, output.getIndex());
		assertEquals(type, output.getType());
		assertEquals(settings.getAnalyzer(), output.getAnalyzer());
		
	}

}
