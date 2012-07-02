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

package fr.issamax.essearch.service.index;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.api.common.RestAPIException;
import fr.issamax.essearch.service.admin.river.RiverService;

@Component
public class RestIndexService {

	private static final long serialVersionUID = 1L;

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	@Autowired Client client;

	/**
	 * Create an index
	 * @param index
	 * @param type
	 * @param analyzer
	 * @return
	 * @throws RestAPIException
	 */
	public Boolean createIndex(String index, String type, String analyzer) throws RestAPIException {

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
	 * Delete an index
	 * @param index
	 */
	public void delete(String index) {
		// TODO Auto-generated method stub
		
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
