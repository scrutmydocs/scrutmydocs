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

package org.scrutmydocs.webapp.configuration;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.node.Node;
import org.scrutmydocs.webapp.api.settings.rivers.fsriver.data.FSRiver;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.scrutmydocs.webapp.helpers.FSRiverHelper;
import org.scrutmydocs.webapp.util.ESHelper;
import org.scrutmydocs.webapp.util.PropertyScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.pilato.spring.elasticsearch.ElasticsearchAbstractClientFactoryBean;
import fr.pilato.spring.elasticsearch.ElasticsearchClientFactoryBean;
import fr.pilato.spring.elasticsearch.ElasticsearchNodeFactoryBean;
import fr.pilato.spring.elasticsearch.ElasticsearchTransportClientFactoryBean;

@Configuration
public class AppConfig {

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	@Bean
	public ScrutMyDocsProperties smdProperties() throws Exception {
		ScrutMyDocsProperties smdProperties = PropertyScanner.scanPropertyFile();
		return smdProperties;
	}

	@Bean
	public Node esNode() throws Exception {
		ScrutMyDocsProperties smdProperties = smdProperties();
		
		if (smdProperties.isNodeEmbedded()) {
			logger.info("Starting embedded Node...");
			ElasticsearchNodeFactoryBean factory = new ElasticsearchNodeFactoryBean();
			factory.afterPropertiesSet();
			return factory.getObject();
		}
		
		return null;
	}

	@Bean
	public Client esClient() throws Exception {
		ScrutMyDocsProperties smdProperties = smdProperties();
		ElasticsearchAbstractClientFactoryBean factory = null;
		if (smdProperties.isNodeEmbedded()) {
			logger.info("Starting client Node...");
			factory = new ElasticsearchClientFactoryBean();
			((ElasticsearchClientFactoryBean) factory).setNode(esNode());
		} else {
			logger.info("Starting client for cluster {} at {} ...", smdProperties.getClusterName(), smdProperties.getNodeAdresses());
			factory = new ElasticsearchTransportClientFactoryBean();
			((ElasticsearchTransportClientFactoryBean) factory).setEsNodes(smdProperties.getNodeAdresses());
		}
		// TODO Manage ES Settings
		// factory.setSettings(settings)
		factory.afterPropertiesSet();
		
		try {
			logger.warn("TODO : remove automatic river creation. Just here for example purpose !");
			// We are going to create the filesystem river if needed
			XContentBuilder xb = FSRiverHelper.toXContent(
					new FSRiver("myfirstriver", SMDSearchProperties.INDEX_NAME, SMDSearchProperties.INDEX_TYPE_DOC, "Scan tmp dir", "/tmp_es", 30L, "standard", false));		
		
			factory.getObject().prepareIndex(SMDSearchProperties.ES_META_INDEX, SMDSearchProperties.ES_META_RIVERS, "myfirstriver").setSource(xb)
					.execute().actionGet();

			// We are going to create a second filesystem river to test multiple feeds
			xb = FSRiverHelper.toXContent(
					new FSRiver("mysecondriver", SMDSearchProperties.INDEX_NAME,
							SMDSearchProperties.INDEX_TYPE_DOC, "Scan second dir", "/tmp_es_second", 30L, "standard", false));		
			
			factory.getObject().prepareIndex(SMDSearchProperties.ES_META_INDEX, SMDSearchProperties.ES_META_RIVERS, "mysecondriver").setSource(xb)
					.execute().actionGet();
			
			ESHelper.createIndexIfNeeded(factory.getObject());

		} catch (ElasticSearchException e) {
			// TODO log.debug("Index is closed");
		}
		
		return factory.getObject();
	}
}
