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
import static org.junit.Assert.assertTrue;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import fr.issamax.essearch.api.data.Document;
import fr.issamax.essearch.api.data.RestResponseDocument;
import fr.issamax.essearch.constant.ESSearchProperties;

public class DocumentApiTest extends AbstractConfigurationIntegrationTest {
	private static final String BASE_URL = "http://localhost:9090/essearch/api/docs/doc/";

	private ESLogger logger = ESLoggerFactory.getLogger(DocumentApiTest.class
			.getName());

	@Autowired
	private RestTemplate restTemplate;

	@Test
	public void push_document() throws Exception {
		Document input = new Document("nom.pdf", "BASE64CODE");

		RestResponseDocument response = restTemplate.postForObject(BASE_URL, input,
				RestResponseDocument.class, new Object[] {});
		assertNotNull(response);
		assertTrue(response.isOk());
		assertNotNull(response.getObject());
		Document output = (Document) response.getObject();
		assertNotNull(output);
		assertNotNull(output.getId());
		assertEquals(ESSearchProperties.INDEX_NAME, output.getIndex());
		assertEquals(ESSearchProperties.INDEX_TYPE_DOC, output.getType());
		input.setId(output.getId());
		assertEquals(input, output);
	}

	@Test
	public void push_then_get_document() throws Exception {
		Document input = new Document("nom2.pdf", "BASE64CODEO");
		RestResponseDocument response = restTemplate.postForObject(BASE_URL, input,
				RestResponseDocument.class, new Object[] {});
		assertNotNull(response);
		assertTrue(response.isOk());
		assertNotNull(response.getObject());
		input = (Document) response.getObject();

		String url = BASE_URL + input.getIndex() + "/" + input.getType() + "/" + input.getId();
		
		Document output = restTemplate.getForObject(url, Document.class);

		assertNotNull(output);
		
		assertEquals(input, output);
	}

}
