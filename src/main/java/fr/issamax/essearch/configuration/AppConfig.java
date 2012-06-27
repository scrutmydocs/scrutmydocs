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

package fr.issamax.essearch.configuration;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.node.Node;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.issamax.essearch.admin.river.data.FSRiver;
import fr.issamax.essearch.admin.river.data.FSRiverHelper;
import fr.issamax.essearch.constant.ESSearchProperties;
import fr.pilato.spring.elasticsearch.ElasticsearchClientFactoryBean;
import fr.pilato.spring.elasticsearch.ElasticsearchNodeFactoryBean;

@Configuration
public class AppConfig {
	@Bean
	public Node esNode() throws Exception {
		ElasticsearchNodeFactoryBean factory = new ElasticsearchNodeFactoryBean();
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	@Bean
	public Client esClient() throws Exception {
		ElasticsearchClientFactoryBean factory = new ElasticsearchClientFactoryBean();
		factory.setNode(esNode());
		factory.afterPropertiesSet();
		
		try {
			// We are going to create the filesystem river if needed
			XContentBuilder xb = FSRiverHelper.toXContent(
					new FSRiver("myfirstriver", ESSearchProperties.INDEX_NAME, ESSearchProperties.INDEX_TYPE_DOC, "Scan tmp dir", "/tmp_es", 30L, "standard", false));		
		
			factory.getObject().prepareIndex(ESSearchProperties.ES_META_INDEX, ESSearchProperties.ES_META_RIVERS, "myfirstriver").setSource(xb)
					.execute().actionGet();

			// We are going to create a second filesystem river to test multiple feeds
			xb = FSRiverHelper.toXContent(
					new FSRiver("mysecondriver", ESSearchProperties.INDEX_NAME,
							ESSearchProperties.INDEX_TYPE_DOC, "Scan second dir", "/tmp_es_second", 30L, "standard", false));		
			
			factory.getObject().prepareIndex(ESSearchProperties.ES_META_INDEX, ESSearchProperties.ES_META_RIVERS, "mysecondriver").setSource(xb)
					.execute().actionGet();

		} catch (ElasticSearchException e) {
			// TODO log.debug("Index is closed");
		}
		
		return factory.getObject();
	}
}
