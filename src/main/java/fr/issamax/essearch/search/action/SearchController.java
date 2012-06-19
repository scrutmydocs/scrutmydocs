package fr.issamax.essearch.search.action;

import java.io.Serializable;
import java.util.List;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.data.Result;
import fr.issamax.essearch.data.Results;
import fr.issamax.essearch.search.data.LazySearch;
import fr.issamax.essearch.search.service.SearchService;

@Component("searchController")
@Scope("session")
public class SearchController implements Serializable {

	private static final long serialVersionUID = -336480920773407570L;

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	@Autowired
	protected SearchService searchService;

	protected String search;

	 protected LazyDataModel<Result> lazySearch; 

//	 protected Results results;

	public void google() {


		this.lazySearch = new LazySearch(searchService, this.search);
	}

	/**
	 * Autocomplete TODO Create multifield mapping with edge n gram TODO Search
	 * and create facets
	 * 
	 * @param query
	 * @return
	 */
	public List<String> complete(String query) {

		return searchService.complete(this.search);
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getSearch() {
		return search;
	}

	public LazyDataModel<Result> getLazySearch() {
		return lazySearch;
	}

	public void setLazySearch(LazyDataModel<Result> lazySearch) {
		this.lazySearch = lazySearch;
	}


}
