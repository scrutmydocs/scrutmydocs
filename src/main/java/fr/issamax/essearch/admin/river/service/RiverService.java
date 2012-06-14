package fr.issamax.essearch.admin.river.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.admin.river.data.FSRiver;
import fr.issamax.essearch.admin.river.data.FSRiverHelper;

@Component
public class RiverService {
	@Autowired Client client;

	/**
	 * Get the river definition by its name
	 * @param name
	 * @return
	 */
	public FSRiver get(String name) {
		if (name == null) return null;
		
		// TODO Be a little more fancy
		List<FSRiver> rivers = get();
		
		for (Iterator<FSRiver> iterator = rivers.iterator(); iterator.hasNext();) {
			FSRiver fsRiver = iterator.next();
			if (name.equals(fsRiver.getName())) return fsRiver;
		}
		
		return null;
	}

	/**
	 * Get all active rivers
	 * @return
	 */
	public List<FSRiver> get() {
		List<FSRiver> rivers = new ArrayList<FSRiver>();
		
		SearchRequestBuilder srb = new SearchRequestBuilder(client);

		try {
			IdsQueryBuilder iqb = new IdsQueryBuilder((String[]) null);
			iqb.addIds("_meta");
			srb.setIndices("_river");
			srb.setQuery(iqb);
			
			SearchResponse response = srb.execute().actionGet();
			
			if (response.hits().totalHits() > 0) {
				
				for (int i = 0; i < response.hits().hits().length; i++) {
					SearchHit hit = response.hits().hits()[i];

					Map<String, Object> meta = (Map<String, Object>) hit.sourceAsMap();
					if (meta.containsKey("fs")) {
						// We only manage FS rivers
						FSRiver fsriver = FSRiverHelper.toFSRiver(meta);
						rivers.add(fsriver);
					}
				}
			}
			
		} catch (IndexMissingException e) {
			// That's a common use case. We started with an empty index
		}
		
		return rivers;
	}

	/**
	 * Update (or add) a river
	 * @param river
	 */
	public void update(FSRiver river) {
		//TODO
		System.out.println("TODO DPI Update ES River : " + river.toString());
	}
	
	/**
	 * Remove river
	 * @param river
	 */
	public void remove(FSRiver river) {
		//TODO
		System.out.println("TODO DPI Remove ES River : " + river.toString());
	}


}
