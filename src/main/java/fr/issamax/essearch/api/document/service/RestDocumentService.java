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

package fr.issamax.essearch.api.document.service;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.admin.river.service.RiverService;
import fr.issamax.essearch.api.common.RestAPIException;
import fr.issamax.essearch.api.document.data.Document;
import fr.issamax.essearch.constant.ESSearchProperties;

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

	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	public Document push(Document document) {
		if (document == null)
			return null;

		if (document.getIndex() == null || document.getIndex().isEmpty()) {
			document.setIndex(ESSearchProperties.INDEX_NAME);
		}
		if (document.getType() == null || document.getType().isEmpty()) {
			document.setType(ESSearchProperties.INDEX_TYPE_DOC);
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
			logger.warn("Can not index document {}", document);
		}

		return document;
	}

	public Boolean createIndex(String index, String type) throws RestAPIException {

		try {
			if (isTypeExist(index, type)) {
				throw new RestAPIException("Type already exists");
			}
			pushMapping(index, type, null);

			return new Boolean(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Boolean(false);
	}

	/**
	 * Check if an index already exists
	 * 
	 * @param index
	 *            Index name
	 * @return true if index already exists
	 * @throws Exception
	 */
	private boolean isTypeExist(String index, String type) throws Exception {
		return client.admin().indices().prepareExists(index, type).execute()
				.actionGet().isExists();
	}

	/**
	 * Define a type for a given index and if exists with its mapping definition
	 * (loaded in classloader)
	 * 
	 * @param index
	 *            Index name
	 * @param type
	 *            Type name
	 * @param xcontent
	 *            If you give an xcontent, it will be used to define the mapping
	 * @throws Exception
	 */
	private void pushMapping(String index, String type, XContentBuilder xcontent)
			throws Exception {
		if (logger.isTraceEnabled())
			logger.trace("pushMapping(" + index + "," + type + ")");

		// If type does not exist, we create it
		boolean mappingExist = isMappingExist(index, type);
		if (!mappingExist) {
			if (logger.isDebugEnabled())
				logger.debug("Mapping [" + index + "]/[" + type
						+ "] doesn't exist. Creating it.");

			String source = null;

			// Read the mapping json file if exists and use it
			if (xcontent == null)
				source = readJsonDefinition(type);

			if (source != null || xcontent != null) {
				PutMappingRequestBuilder pmrb = client.admin().indices()
						.preparePutMapping(index).setType(type);

				if (source != null) {
					if (logger.isTraceEnabled())
						logger.trace("Mapping for [" + index + "]/[" + type
								+ "]=" + source);
					pmrb.setSource(source);
				}

				if (xcontent != null) {
					if (logger.isTraceEnabled())
						logger.trace("Mapping for [" + index + "]/[" + type
								+ "]=" + xcontent.string());
					pmrb.setSource(xcontent);
				}

				// Create type and mapping
				PutMappingResponse response = pmrb.execute().actionGet();
				if (!response.acknowledged()) {
					throw new Exception("Could not define mapping for type ["
							+ index + "]/[" + type + "].");
				} else {
					if (logger.isDebugEnabled())
						logger.debug("Mapping definition for [" + index + "]/["
								+ type + "] succesfully created.");
				}
			} else {
				if (logger.isDebugEnabled())
					logger.debug("No mapping definition for [" + index + "]/["
							+ type + "]. Ignoring.");
			}
		} else {
			if (logger.isDebugEnabled())
				logger.debug("Mapping [" + index + "]/[" + type
						+ "] already exists.");
		}
		if (logger.isTraceEnabled())
			logger.trace("/pushMapping(" + index + "," + type + ")");
	}

	/**
	 * Check if a mapping already exists in an index
	 * 
	 * @param index
	 *            Index name
	 * @param type
	 *            Mapping name
	 * @return true if mapping exists
	 */
	private boolean isMappingExist(String index, String type) {
		ClusterState cs = client.admin().cluster().prepareState()
				.setFilterIndices(index).execute().actionGet().getState();
		IndexMetaData imd = cs.getMetaData().index(index);

		if (imd == null)
			return false;

		MappingMetaData mdd = imd.mapping(type);

		if (mdd != null)
			return true;
		return false;
	}

	/**
	 * Read the mapping for a type.<br>
	 * Shortcut to readFileInClasspath("/estemplate/" + type + ".json");
	 * 
	 * @param type
	 *            Type name
	 * @return Mapping if exists. Null otherwise.
	 * @throws Exception
	 */
	private String readJsonDefinition(String type) throws Exception {
		return readFileInClasspath("/estemplate/" + type + ".json");
	}

	/**
	 * Read a file in classpath and return its content
	 * 
	 * @param url
	 *            File URL Example : /es/twitter/_settings.json
	 * @return File content or null if file doesn't exist
	 * @throws Exception
	 */
	private static String readFileInClasspath(String url) throws Exception {
		StringBuffer bufferJSON = new StringBuffer();

		try {
			InputStream ips = RiverService.class.getResourceAsStream(url);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;

			while ((line = br.readLine()) != null) {
				bufferJSON.append(line);
			}
			br.close();
		} catch (Exception e) {
			return null;
		}

		return bufferJSON.toString();
	}

}
