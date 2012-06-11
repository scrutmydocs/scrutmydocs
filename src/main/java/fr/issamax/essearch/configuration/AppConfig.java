package fr.issamax.essearch.configuration;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.node.Node;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
		XContentBuilder xb = jsonBuilder()
				.startObject()
					.field("type", "fs")
					.startObject("fs")
						.startArray("fs")
							.startObject()
								.field("name", "tmp")
								.field("url", "/tmp_es")
								.field("update_rate", 30 * 1000)
							.endObject()
						.endArray()
					.endObject()
				.endObject();		
		
		factory.getObject().prepareIndex("_river", ESSearchProperties.INDEX_NAME, "_meta").setSource(xb)
				.execute().actionGet();
		
		return factory.getObject();
	}
}
