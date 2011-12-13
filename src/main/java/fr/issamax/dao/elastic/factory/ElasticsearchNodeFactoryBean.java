package fr.issamax.dao.elastic.factory;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.common.settings.ImmutableSettings.Builder;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;


/**
 * A {@link FactoryBean} implementation used to create a {@link Node} element
 * which is an embedded instance of the cluster within a running application.
 * <p>
 * This factory allows for defining custom configuration via the
 * {@link #setConfigLocation(Resource)} or {@link #setConfigLocations(List)}
 * property setters.
 * <p>
 * <b>Note</b>: multiple configurations can be "accumulated" since
 * {@link Builder#loadFromStream(String, java.io.InputStream)} doesn't replace
 * but adds to the map (this also means that loading order of configuration
 * files matters).
 * <p>
 * In addition Spring's property mechanism can be used via
 * {@link #setSettings(Map)} property setter which allows for local settings to
 * be configured via Spring.
 * <p>
 * The lifecycle of the underlying {@link Node} instance is tied to the
 * lifecycle of the bean via the {@link #destroy()} method which calls
 * {@link Node#close()}
 * 
 * @author Erez Mazor (erezmazor@gmail.com)
 */
public class ElasticsearchNodeFactoryBean implements FactoryBean<Node>,
		InitializingBean, DisposableBean {

	protected final Log logger = LogFactory.getLog(getClass());

	private List<Resource> configLocations;

	private Resource configLocation;

	private Map<String, String> settings;

	private Node node;


	public void setConfigLocation(final Resource configLocation) {
		this.configLocation = configLocation;
	}

	public void setConfigLocations(final List<Resource> configLocations) {
		this.configLocations = configLocations;
	}

	public void setSettings(final Map<String, String> settings) {
		this.settings = settings;
	}

//	@Override
	public void afterPropertiesSet() throws Exception {
		internalCreateNode();
	}

	private void internalCreateNode() {
		final NodeBuilder nodeBuilder = NodeBuilder.nodeBuilder();

		if (null != configLocation) {
			internalLoadSettings(nodeBuilder, configLocation);
		}

		if (null != configLocations) {
			for (final Resource location : configLocations) {
				internalLoadSettings(nodeBuilder, location);
			}
		}

		if (null != settings) {
			nodeBuilder.getSettings().put(settings);
		}
		
		if (logger.isDebugEnabled()) logger.debug("Starting ElasticSearch node...");
		node = nodeBuilder.node();
		logger.info( "Node [" + node.settings().get("name") + "] for [" + node.settings().get("cluster.name") + "] cluster started..." );
		if (logger.isDebugEnabled()) logger.debug( "  - data : " + node.settings().get("path.data") );
		if (logger.isDebugEnabled()) logger.debug( "  - logs : " + node.settings().get("path.logs") );
	}

	private void internalLoadSettings(final NodeBuilder nodeBuilder,
			final Resource configLocation) {

		try {
			final String filename = configLocation.getFilename();
			if (logger.isInfoEnabled()) {
				logger.info("Loading configuration file from: " + filename);
			}
			nodeBuilder.getSettings().loadFromStream(filename,
					configLocation.getInputStream());
		} catch (final Exception e) {
			throw new IllegalArgumentException(
					"Could not load settings from configLocation: "
							+ configLocation.getDescription(), e);
		}
	}

	@Override
	public void destroy() throws Exception {
		try {
			logger.info("Closing ElasticSearch node");
			node.close();
		} catch (final Exception e) {
			logger.error("Error closing Elasticsearch node: ", e);
		}
	}

	@Override
	public Node getObject() throws Exception {
		return node;
	}

	@Override
	public Class<Node> getObjectType() {
		return Node.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
