package fr.issamax.dao.elastic.factory;

import static fr.issamax.dao.elastic.factory.ESSearchProperties.DIR_FIELD_NAME;
import static fr.issamax.dao.elastic.factory.ESSearchProperties.DIR_FIELD_PATH_ENCODED;
import static fr.issamax.dao.elastic.factory.ESSearchProperties.DIR_FIELD_ROOT_PATH;
import static fr.issamax.dao.elastic.factory.ESSearchProperties.DIR_FIELD_VIRTUAL_PATH;
import static fr.issamax.dao.elastic.factory.ESSearchProperties.DOC_FIELD_DATE;
import static fr.issamax.dao.elastic.factory.ESSearchProperties.DOC_FIELD_NAME;
import static fr.issamax.dao.elastic.factory.ESSearchProperties.DOC_FIELD_PATH_ENCODED;
import static fr.issamax.dao.elastic.factory.ESSearchProperties.DOC_FIELD_ROOT_PATH;
import static fr.issamax.dao.elastic.factory.ESSearchProperties.DOC_FIELD_VIRTUAL_PATH;
import static fr.issamax.dao.elastic.factory.ESSearchProperties.INDEX_NAME;
import static fr.issamax.dao.elastic.factory.ESSearchProperties.INDEX_TYPE_DOC;
import static fr.issamax.dao.elastic.factory.ESSearchProperties.INDEX_TYPE_FOLDER;
import static fr.issamax.dao.elastic.factory.ESSearchProperties.INDEX_TYPE_FS;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.IndicesExistsRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.node.Node;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * A {@link FactoryBean} implementation used to create a {@link Client} element
 * from a {@link Node}.
 * <p>
 * The lifecycle of the underlying {@link Client} instance is tied to the
 * lifecycle of the bean via the {@link #destroy()} method which calls
 * {@link Client#close()}
 * 
 * @author David Pilato
 */
public abstract class ElasticsearchAbstractClientFactoryBean implements FactoryBean<Client>,
		InitializingBean, DisposableBean {

	protected final Log logger = LogFactory.getLog(getClass());

	private Client client;

	private boolean reinitMapping;

	/**
	 * Implement this method to build a client
	 * @return ES Client
	 * @throws Exception if something goes wrong
	 */
	abstract protected Client buildClient() throws Exception;
	
	public void setReinitMapping(boolean reinitMapping) {
		this.reinitMapping = reinitMapping;
	}
	
	public boolean isReinitMapping() {
		return reinitMapping;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("Starting ElasticSearch client");
		client = buildClient();
		initMapping();
	}

	@Override
	public void destroy() throws Exception {
		try {
			logger.info("Closing ElasticSearch client");
			if (client != null) {
				client.close();
			}
		} catch (final Exception e) {
			logger.error("Error closing Elasticsearch client: ", e);
		}
	}

	@Override
	public Client getObject() throws Exception {
		return client;
	}

	@Override
	public Class<Client> getObjectType() {
		return Client.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Init mapping if needed.
	 * <p>Note that you can force to reinit mapping using {@link #setReinitMapping(boolean)}
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ElasticSearchException
	 * @throws ExecutionException
	 */
	public void initMapping() throws IOException, InterruptedException,
			ElasticSearchException, ExecutionException {

		if (isReinitMapping()) {
			try {
				client.admin().indices().delete(new DeleteIndexRequest(INDEX_NAME)).actionGet();
			} catch (Exception e) {
				logger.warn("Can not delete index" + e.getMessage());
			}
		}

		// Creating the index
		if (!client.admin().indices()
				.exists(new IndicesExistsRequest(INDEX_NAME)).get().exists()) {

			client.admin().indices().create(new CreateIndexRequest(INDEX_NAME))
					.actionGet();

			XContentBuilder xbMapping = jsonBuilder().startObject()
					.startObject(INDEX_TYPE_DOC).startObject("properties")
					.startObject(DOC_FIELD_NAME).field("type", "string").field("analyzer","keyword").endObject()
					.startObject(DOC_FIELD_PATH_ENCODED).field("type", "string").field("analyzer","keyword").endObject()
					.startObject(DOC_FIELD_ROOT_PATH).field("type", "string").field("analyzer","keyword").endObject()
					.startObject(DOC_FIELD_VIRTUAL_PATH).field("type", "string").field("analyzer","keyword").endObject()
					.startObject(DOC_FIELD_DATE).field("type", "date").endObject()
					.startObject("file").field("type", "attachment")
					.startObject("fields").startObject("title")
					.field("store", "yes").endObject().startObject("file")
					.field("term_vector", "with_positions_offsets")
					.field("store", "yes").endObject().endObject().endObject()
					.endObject().endObject().endObject();

			client.admin().indices().preparePutMapping(INDEX_NAME)
					.setType(INDEX_TYPE_DOC).setSource(xbMapping).execute()
					.actionGet();

			/*** FS RIVER ***/
			xbMapping = jsonBuilder().startObject()
					.startObject(INDEX_TYPE_FS).startObject("properties")
					.startObject("scanDate").field("type", "long").endObject()
					.startObject("folders").startObject("properties")
						.startObject("url").field("type", "string").endObject()
					.endObject().endObject()
					.endObject().endObject().endObject();

			client.admin()
					.indices()
					.preparePutMapping(INDEX_NAME)
					.setType(INDEX_TYPE_FS).setSource(xbMapping).execute()
					.actionGet();

			/*** FOLDER ***/
			xbMapping = jsonBuilder().startObject()
					.startObject(INDEX_TYPE_FOLDER).startObject("properties")
					.startObject(DIR_FIELD_NAME).field("type", "string").field("analyzer","keyword").endObject()
					.startObject(DIR_FIELD_PATH_ENCODED).field("type", "string").field("analyzer","keyword").endObject()
					.startObject(DIR_FIELD_ROOT_PATH).field("type", "string").field("analyzer","keyword").endObject()
					.startObject(DIR_FIELD_VIRTUAL_PATH).field("type", "string").field("analyzer","keyword").endObject()
					.endObject().endObject().endObject();

			client.admin()
					.indices()
					.preparePutMapping(
							INDEX_NAME)
					.setType(INDEX_TYPE_FOLDER).setSource(xbMapping).execute()
					.actionGet();

		}
	}
}
