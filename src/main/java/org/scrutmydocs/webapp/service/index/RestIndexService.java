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

package org.scrutmydocs.webapp.service.index;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.scrutmydocs.webapp.api.common.RestAPIException;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.scrutmydocs.webapp.util.ESHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


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
		if (logger.isDebugEnabled()) logger.debug("createIndex({}, {}, {})", index, type, analyzer);
		Boolean output = false;
		try {
			if (ESHelper.isTypeExist(client, index, type)) {
				throw new RestAPIException("Type already exists");
			}
			
			ESHelper.pushMapping(client, index, type, null);
			
			output = true;
		} catch (Exception e) {
			logger.error("Can not create Index({}, {}, {}) : {}", index, type, analyzer, e.getMessage());
		}
		if (logger.isDebugEnabled()) logger.debug("/createIndex({}, {}, {})={}", index, type, analyzer, output);
		return output;
	}

	/**
	 * Delete an index
	 * @param index
	 */
	public void delete(String index) throws RestAPIException {
		if (logger.isDebugEnabled()) logger.debug("delete({})", index);
		try {
			DeleteIndexResponse dir = client.admin().indices().prepareDelete(index).execute().actionGet();
			if (!dir.acknowledged()) throw new RestAPIException("ES did not acknowledge index removal...");
		} catch (Exception e) {
			logger.error("Can not delete Index({}) : {}", index, e.getMessage());
			throw new RestAPIException("Error while removing index : " + e.getMessage());
		}
		if (logger.isDebugEnabled()) logger.debug("/delete({})", index);
	}
}
