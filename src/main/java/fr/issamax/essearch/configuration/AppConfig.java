package fr.issamax.essearch.configuration;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

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
		
		
		// We are going to create the filesystem river if needed
		XContentBuilder xb = FSRiverHelper.toXContent(ESSearchProperties.INDEX_NAME,
				ESSearchProperties.INDEX_TYPE_DOC,
				new FSRiver("fs", "tmp", "/tmp_es", 30L));		
		
		factory.getObject().prepareIndex("_river", "myfirstriver", "_meta").setSource(xb)
				.execute().actionGet();

		// We add a dummy river to test
		xb = jsonBuilder()
			.startObject()
				.field("type", "dummy")
			.endObject();
		
		
		factory.getObject().prepareIndex("_river", "dummy", "_meta").setSource(xb)
				.execute().actionGet();

		
		return factory.getObject();
	}
}
