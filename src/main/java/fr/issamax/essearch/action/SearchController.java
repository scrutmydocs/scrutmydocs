package fr.issamax.essearch.action;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import java.io.Serializable;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.data.Results;

@Component("searchController")
@Scope("request")
public class SearchController implements Serializable{

	@Autowired
	protected Client esClient;

	protected String search;
	
	protected Results results;

	public void google() {
		try {
			QueryBuilder qb;
			if (search == null || search.trim().length() <= 0) {
				qb = matchAllQuery();
			} else {
				qb = termQuery("_all", search);
			}
			
			 SearchResponse searchHits = esClient.prepareSearch()
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setQuery(qb).setFrom(0).setSize(10)
					.addHighlightedField("name").addHighlightedField("file")
					.setHighlighterPreTags("<b>")
					.setHighlighterPostTags("</b>").execute().actionGet();

			 results = new Results(searchHits);

			

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

	public Results getResults() {
		return results;
	}

	public void setResults(Results results) {
		this.results = results;
	}

}
