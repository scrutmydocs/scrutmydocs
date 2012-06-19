package fr.issamax.essearch.search.action;

import java.io.Serializable;
import java.util.List;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.search.data.LazySearch;
import fr.issamax.essearch.search.service.SearchService;

@Component("searchController")
@Scope("request")
public class SearchController implements Serializable {

	private static final long serialVersionUID = -336480920773407570L;

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	@Autowired
	protected SearchService searchService;

	protected String search;

	@Autowired
	protected LazySearch lazySearch; 

	public void google() {
		this.lazySearch.setSearch(search);
	}

	/**
	 * Autocomplete TODO Create multifield mapping with edge n gram TODO Search
	 * and create facets
	 * 
	 * @param query
	 * @return
	 */
	public List<String> complete(String query) {
		return searchService.complete(query);
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getSearch() {
		return search;
	}

	public LazySearch getLazySearch() {
		return lazySearch;
	}

	public void setLazySearch(LazySearch lazySearch) {
		this.lazySearch = lazySearch;
	}
}
