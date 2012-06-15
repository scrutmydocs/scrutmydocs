package fr.issamax.essearch.admin.river.service;

import java.io.Serializable;

import org.elasticsearch.action.admin.indices.close.CloseIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.admin.river.data.FSRiver;
import fr.issamax.essearch.admin.river.data.FSRiverHelper;

@Component
public class RiverService implements Serializable {
	private static final long serialVersionUID = 1L;

	private ESLogger logger = Loggers.getLogger(getClass().getName());
	
	@Autowired Client client;

	/**
	 * Update (or add) a river
	 * @param river
	 */
	public void add(FSRiver river) {
		if (logger.isDebugEnabled()) logger.debug("add({})", river);
		// We only add the river if the river is started
		if (river == null || !river.isStart()) return;
		
		XContentBuilder xb = FSRiverHelper.toXContent(river);		
		
		try {
			client.prepareIndex("_river", river.getId(), "_meta").setSource(xb)
					.execute().actionGet();
		} catch (Exception e) {
			logger.warn("add({}) : Exception raised : {}", river, e.getClass());
			if (logger.isDebugEnabled()) logger.debug("- Exception stacktrace :", e);
		}
		if (logger.isDebugEnabled()) logger.debug("/add({})", river);
	}
	
	/**
	 * Remove river
	 * @param river
	 */
	public void delete(FSRiver river) {
		if (logger.isDebugEnabled()) logger.debug("delete({})", river);
		if (river == null) return;
		
		try {
			client.admin().indices().prepareDeleteMapping("_river").setType(river.getId()).execute().actionGet();
		} catch (Exception e) {
			logger.warn("delete({}) : Exception raised : {}", river, e.getClass());
			if (logger.isDebugEnabled()) logger.debug("- Exception stacktrace :", e);
		}
		if (logger.isDebugEnabled()) logger.debug("/delete({})", river);
	}

	/**
	 * Stop all rivers
	 */
	public void stop() {
		if (logger.isDebugEnabled()) logger.debug("stop()");
		CloseIndexRequestBuilder irb = new CloseIndexRequestBuilder(client.admin().indices());

		irb.setIndex("_river");
		CloseIndexResponse response = irb.execute().actionGet();
		
		if (!response.acknowledged()) {
			logger.warn("stop() : Pb when closing rivers.");
		}
		if (logger.isDebugEnabled()) logger.debug("/stop()");
	}
	
	/**
	 * (Re)Start all rivers
	 */
	public void start() {
		if (logger.isDebugEnabled()) logger.debug("start()");
		OpenIndexRequestBuilder irb = new OpenIndexRequestBuilder(client.admin().indices());

		irb.setIndex("_river");
		OpenIndexResponse response = irb.execute().actionGet();
		
		if (!response.acknowledged()) {
			logger.warn("start() : Pb when starting rivers.");
		}
		if (logger.isDebugEnabled()) logger.debug("/start()");
	}
	

}
