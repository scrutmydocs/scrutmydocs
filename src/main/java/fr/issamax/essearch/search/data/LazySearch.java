package fr.issamax.essearch.search.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import fr.issamax.essearch.data.Result;
import fr.issamax.essearch.data.Results;
import fr.issamax.essearch.search.service.SearchService;

/**
 * Dummy implementation of LazyDataModel that uses a list to mimic a real
 * datasource like a database.
 */

public class LazySearch extends LazyDataModel<Result> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7038031891023629994L;

	protected SearchService searchService;

	protected String search;

	private Results results;

	public LazySearch(SearchService searchService, String search) {
		this.search = search;
		this.searchService = searchService;
	}

	@Override
	public Result getRowData(String rowKey) {

		for (Result result : results.getResults()) {
			if (result.getTitle().equals(rowKey))
				return result;
		}

		return null;
	}

	@Override
	public Object getRowKey(Result result) {
		return result.getTitle();
	}

	@Override
	public List<Result> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, String> filters) {

		results = searchService.google(this.search, first, pageSize, sortField,
				sortOrder, filters);

		this.setRowCount(new Long(results.getSearchResponse().getHits()
				.getTotalHits()).intValue());
		
		return results.getResults();
	}

	public SearchService getSearchService() {
		return searchService;
	}

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public Results getResults() {
		return results;
	}

	public void setResults(Results results) {
		this.results = results;
	}

}
