package fr.issamax.test.essearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.pilato.spring.elasticsearch.ElasticsearchClientFactoryBean;
import fr.pilato.spring.elasticsearch.ElasticsearchNodeFactoryBean;

@Configuration
public class AppTestConfig {
	
	
	@Bean
	public Node esNode() throws Exception {
		ElasticsearchNodeFactoryBean factory = new ElasticsearchNodeFactoryBean();
		factory.setSettingsFile("es-test.properties");
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	@Bean
	public Client esClient() throws Exception {
		ElasticsearchClientFactoryBean factory = new ElasticsearchClientFactoryBean();
		factory.setNode(esNode());
		factory.afterPropertiesSet();
		return factory.getObject();
		}


}
