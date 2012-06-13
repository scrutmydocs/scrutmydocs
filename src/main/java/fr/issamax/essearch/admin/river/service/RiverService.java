package fr.issamax.essearch.admin.river.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.elasticsearch.action.admin.indices.status.IndicesStatusRequestBuilder;
import org.elasticsearch.action.admin.indices.status.IndicesStatusResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.indices.IndexMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.admin.river.data.FSRiver;

@Component
public class RiverService {
	@Autowired Client client;

	/**
	 * Get the river definition by its name
	 * @param name
	 * @return
	 */
	public FSRiver get(String name) {
		// TODO DPI Implement here
		return new FSRiver("fs", "dummy", "/dummy", 1000L);
	}

	/**
	 * Get all active rivers
	 * @return
	 */
	public List<FSRiver> get() {
		List<FSRiver> rivers = new ArrayList<FSRiver>();
		
		IndicesStatusRequestBuilder rb = new IndicesStatusRequestBuilder(client.admin().indices()).setIndices("_river");

		try {
			IndicesStatusResponse response =  rb.execute().actionGet();
			// System.out.println(response.indices());
		} catch (IndexMissingException e) {
			// That's a common use case. We started with an empty index
		}
		
		// TODO DPI Implement here
		if (rivers.size() == 0) rivers.add(get(""));
		
		return rivers;
	}

	/**
	 * Update (or add) a river
	 * @param river
	 */
	public void update(FSRiver river) {
		System.out.println(river.toString());
	}


}
