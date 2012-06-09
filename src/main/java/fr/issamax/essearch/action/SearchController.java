package fr.issamax.essearch.action;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryString;
import static fr.issamax.essearch.constant.ESSearchProperties.*;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.data.Results;

@Component("searchController")
@Scope("session")
public class SearchController implements Serializable {
	private static final long serialVersionUID = 1L;

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
				qb = queryString(search);
			}
			
			 SearchResponse searchHits = esClient.prepareSearch()
					.setIndices(INDEX_NAME)
					.setTypes(INDEX_TYPE_DOC)
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

	public void imlucky() {
		try {
			QueryBuilder qb;
			if (search == null || search.trim().length() <= 0) {
				qb = matchAllQuery();
			} else {
				qb = queryString(search);
			}
			
			 SearchResponse searchResponse = esClient.prepareSearch()
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setQuery(qb).setFrom(0).setSize(10)
					.addHighlightedField("name").addHighlightedField("file")
					.setHighlighterPreTags("<b>")
					.setHighlighterPostTags("</b>").execute().actionGet();

			 SearchHits hits = searchResponse.getHits();
			 
			 if (hits != null && hits.getTotalHits() > 0) {
				 SearchHit hit = hits.getAt(0);
				FacesContext.getCurrentInstance().getExternalContext().redirect("api/download/"+hit.getId());
			 }
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
