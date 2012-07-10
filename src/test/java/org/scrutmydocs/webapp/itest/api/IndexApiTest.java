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

package org.scrutmydocs.webapp.itest.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.scrutmydocs.webapp.api.index.data.Index;
import org.scrutmydocs.webapp.api.index.data.RestResponseIndex;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;


/**
 * Test for module "index/"
 * @author David Pilato
 */
public class IndexApiTest extends AbstractApiTest {
	/**
	 * module is "index/"
	 */
	@Override
	protected String getModuleApiUrl() {
		return "index/";
	}
	
	@Test
	public void create_index_with_defaults() throws Exception {
		RestResponseIndex response = restTemplate.postForObject(buildFullApiUrl(), new Index(),
				RestResponseIndex.class, new Object[] {});
		assertNotNull(response);
		assertTrue(response.isOk());
		assertNotNull(response.getObject());
		Index output = (Index) response.getObject();
		assertNotNull(output);
		assertEquals(SMDSearchProperties.INDEX_NAME, output.getIndex());
		assertEquals(SMDSearchProperties.INDEX_TYPE_DOC, output.getType());
		assertNull(output.getAnalyzer());
	}

	@Test
	public void create_index_with_values_in_object() throws Exception {
		String index = "myindex";
		String type = "mytype";
		String analyzer = "myanalyzer";
		Index input = new Index(index, type, analyzer);

		RestResponseIndex response = restTemplate.postForObject(buildFullApiUrl(), input,
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

		String url = buildFullApiUrl(index + "/" + type);
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
		
		String url = buildFullApiUrl(index + "/" + type);
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
