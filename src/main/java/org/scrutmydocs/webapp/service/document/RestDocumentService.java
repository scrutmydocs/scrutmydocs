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

package org.scrutmydocs.webapp.service.document;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.util.Date;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.scrutmydocs.webapp.api.common.RestAPIException;
import org.scrutmydocs.webapp.api.document.data.Document;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RestDocumentService {

	private static final long serialVersionUID = 1L;

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	@Autowired
	Client client;

	@SuppressWarnings("unchecked")
	public Document get(String index, String type, String id) {
		GetResponse responseEs = client.prepareGet(index, type, id).execute()
				.actionGet();
		if (!responseEs.isExists()) {
			return null;
		}

		Document response = new Document();

		Map<String, Object> attachment = (Map<String, Object>) responseEs
				.getSource().get("file");

		response.setContent((String) attachment.get("content"));
		response.setName((String) attachment.get("_name"));
		response.setContentType((String) attachment.get("_content_type"));
		response.setId(responseEs.getId());
		response.setIndex(responseEs.getIndex());
		response.setType(responseEs.getType());

		return response;
	}

	public boolean delete(String index, String type, String id) {
		if (index == null) {
			index = SMDSearchProperties.INDEX_NAME;
		}
		if (type == null) {
			type = SMDSearchProperties.INDEX_TYPE_DOC;
		}

		DeleteResponse response = client.prepareDelete(index, type, index)
				.execute().actionGet();

		return response.isNotFound();
	}

	public Document push(Document document) throws RestAPIException {
		if (logger.isDebugEnabled()) logger.debug("push({})", document);
		if (document == null)
			return null;

		if (document.getIndex() == null || document.getIndex().isEmpty()) {
			document.setIndex(SMDSearchProperties.INDEX_NAME);
		}
		if (document.getType() == null || document.getType().isEmpty()) {
			document.setType(SMDSearchProperties.INDEX_TYPE_DOC);
		}
		try {
			IndexResponse response = client
					.prepareIndex(document.getIndex(), document.getType(),
							document.getId())
					.setSource(
							jsonBuilder()
									.startObject()
									.field("name", document.getName())
									.field("postDate", new Date())
									.startObject("file")
									.field("_content_type",
											document.getContentType())
									.field("_name", document.getName())
									.field("content", document.getContent())
									.endObject().endObject()).execute()
					.actionGet();

			document.setId(response.getId());
		} catch (Exception e) {
			logger.warn("Can not index document {}", document.getName());
			throw new RestAPIException("Can not index document : "+ document.getName() + ": "+e.getMessage());
		}

		if (logger.isDebugEnabled()) logger.debug("/push()={}", document);
		return document;
	}

}
