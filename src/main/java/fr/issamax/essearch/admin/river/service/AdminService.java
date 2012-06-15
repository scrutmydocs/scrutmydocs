package fr.issamax.essearch.admin.river.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.admin.river.data.FSRiver;
import fr.issamax.essearch.admin.river.data.FSRiverHelper;
import fr.issamax.essearch.constant.ESSearchProperties;

@Component
public class AdminService implements Serializable {
	private static final long serialVersionUID = 1L;

	private ESLogger logger = Loggers.getLogger(getClass().getName());
	
	@Autowired Client client;

	/**
	 * Get the river definition by its name
	 * @param name
	 * @return
	 */
	public FSRiver get(String name) {
		if (logger.isDebugEnabled()) logger.debug("get({})", name);
		
		FSRiver fsriver = null;
		
		if (name != null) {
			GetRequestBuilder rb = new GetRequestBuilder(client, ESSearchProperties.ES_META_INDEX);
			rb.setType(ESSearchProperties.ES_META_RIVERS);
			rb.setId(name);
			
			GetResponse response = rb.execute().actionGet();
			if (response.exists()) {
				fsriver = FSRiverHelper.toFSRiver(response.sourceAsMap());
			}
		}

		if (logger.isDebugEnabled()) logger.debug("/get({})={}", name, fsriver);
		return fsriver;
	}

	/**
	 * Get all active rivers
	 * @return
	 */
	public List<FSRiver> get() {
		if (logger.isDebugEnabled()) logger.debug("get()");
		List<FSRiver> rivers = new ArrayList<FSRiver>();
		
		SearchRequestBuilder srb = new SearchRequestBuilder(client);

		try {
			srb.setIndices(ESSearchProperties.ES_META_INDEX);
			srb.setTypes(ESSearchProperties.ES_META_RIVERS);
			
			SearchResponse response = srb.execute().actionGet();
			
			if (response.hits().totalHits() > 0) {
				
				for (int i = 0; i < response.hits().hits().length; i++) {
					SearchHit hit = response.hits().hits()[i];

					// We only manage FS rivers
					FSRiver fsriver = FSRiverHelper.toFSRiver(hit.sourceAsMap());
					rivers.add(fsriver);
				}
			}
			
		} catch (IndexMissingException e) {
			// That's a common use case. We started with an empty index
		}
		
		if (logger.isDebugEnabled()) logger.debug("/get()={}", rivers);
		return rivers;
	}

	/**
	 * Update (or add) a river
	 * @param river
	 */
	public void update(FSRiver river) {
		if (logger.isDebugEnabled()) logger.debug("update({})", river);
		XContentBuilder xb = FSRiverHelper.toXContent(river);		
		
		try {
			client.prepareIndex(ESSearchProperties.ES_META_INDEX, ESSearchProperties.ES_META_RIVERS, river.getId()).setSource(xb)
					.execute().actionGet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (logger.isDebugEnabled()) logger.debug("/update({})", river);
	}
	
	/**
	 * Remove river
	 * @param river
	 */
	public void remove(FSRiver river) {
		if (logger.isDebugEnabled()) logger.debug("remove({})", river);
		try {
			client.prepareDelete(ESSearchProperties.ES_META_INDEX, ESSearchProperties.ES_META_RIVERS, river.getId()).execute().actionGet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (logger.isDebugEnabled()) logger.debug("/remove({})", river);
	}

}
