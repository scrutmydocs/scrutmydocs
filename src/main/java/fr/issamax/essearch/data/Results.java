package fr.issamax.essearch.data;

import java.util.ArrayList;
import java.util.Collection;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;

public class Results {

	protected Collection<Result> results=new ArrayList<Result>();
	protected String took;

	public Results(SearchResponse SearchResponse) {

		this.took = SearchResponse.getTook().format();

		for (SearchHit searchHit : SearchResponse.getHits()) {

			Result result = new Result();
			searchHit.getHighlightFields();
			searchHit.getId();
			result.setTitle(searchHit.getSource().get("name").toString());

			if (searchHit.getHighlightFields() != null) {
				for (HighlightField highlightField : searchHit
						.getHighlightFields().values()) {

					String[] fragmentsBuilder = highlightField.getFragments();

					for (String fragment : fragmentsBuilder) {
						result.getFragments().add(fragment);
					}
				}
			}

			this.getResults().add(result);
		}
	}

	public String getTook() {
		return took;
	}

	public void setTook(String took) {
		this.took = took;
	}

	public Collection<Result> getResults() {
		return results;
	}

	public void setResults(Collection<Result> results) {
		this.results = results;
	}
}
