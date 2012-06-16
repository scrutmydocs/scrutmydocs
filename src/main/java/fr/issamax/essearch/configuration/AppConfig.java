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
					new FSRiver("myfirstriver", ESSearchProperties.INDEX_NAME, ESSearchProperties.INDEX_TYPE_DOC, "fs", "Scan tmp dir", "/tmp_es", 30L, "standard", false));		
		
			factory.getObject().prepareIndex(ESSearchProperties.ES_META_INDEX, ESSearchProperties.ES_META_RIVERS, "myfirstriver").setSource(xb)
					.execute().actionGet();

			// We are going to create a second filesystem river to test multiple feeds
			xb = FSRiverHelper.toXContent(
					new FSRiver("mysecondriver", ESSearchProperties.INDEX_NAME,
							ESSearchProperties.INDEX_TYPE_DOC, "fs", "Scan second dir", "/tmp_es_second", 30L, "standard", false));		
			
			factory.getObject().prepareIndex(ESSearchProperties.ES_META_INDEX, ESSearchProperties.ES_META_RIVERS, "mysecondriver").setSource(xb)
					.execute().actionGet();

		} catch (ElasticSearchException e) {
			// TODO log.debug("Index is closed");
		}
		
		return factory.getObject();
	}
}
