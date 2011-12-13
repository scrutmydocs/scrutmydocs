package fr.issamax.essearch.action;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.ArrayList;
import java.util.Collection;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("searchController")
@Scope("request")
public class SearchController {

	@Autowired
	Client esClient;

	String search;

	Collection<String> hits = null;

	public void google() {

		try {
			SearchResponse response = esClient.prepareSearch()
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setQuery(termQuery("_all", search)).setFrom(0).setSize(10)
					.addHighlightedField("name")
					.addHighlightedField("file")
					.execute().actionGet();

			SearchHits searchHits = response.hits();
			hits = new ArrayList<String>();

			for (SearchHit searchHit : searchHits) {
				StringBuffer sb = new StringBuffer();
				searchHit.getHighlightFields();
				if (searchHit.getHighlightFields() != null) {
					for (HighlightField highlightField : searchHit.getHighlightFields().values()) {
						sb.append(highlightField.toString());
					}
					
				}
				
				hits.add(sb.toString());
			}
			// for( SearchHit searchHit; hits.hits();
			// SearchHit searchHit;
			// searchHit.sourceAsString()
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getSearch() {
		return search;
	}

	public void setHits(Collection<String> hits) {
		this.hits = hits;
	}

	public Collection<String> getHits() {
		return hits;
	}
}
