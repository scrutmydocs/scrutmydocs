package fr.issamax.dao.elastic.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.FactoryBean;

/**
 * A {@link FactoryBean} implementation used to create a {@link TransportClient} element.
 * <p>
 * The lifecycle of the underlying {@link Client} instance is tied to the
 * lifecycle of the bean via the {@link #destroy()} method which calls
 * {@link Client#close()}
 * <p>
 * If you use this client, you must start your own node with es-search cluster name
 * and attachment-plugin
 * @author David Pilato
 */
public class ElasticsearchTransportClientFactoryBean extends ElasticsearchAbstractClientFactoryBean {

	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	protected Client buildClient() throws Exception {
		Settings settings = ImmutableSettings.settingsBuilder()
				.loadFromClasspath("es.properties").build();
		
		// TODO use properties
		Client client = new TransportClient(settings)
	        .addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
		return client;
	}
}
