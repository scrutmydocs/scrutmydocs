package fr.issamax.essearch.data;

import java.util.ArrayList;
import java.util.Collection;

import org.elasticsearch.search.SearchHit;

public class Result {
	protected String title;
	protected Collection<String> fragments=new ArrayList<String>();
	protected SearchHit searchHit;
	
	public Result(SearchHit searchHit) {
		this.searchHit = searchHit;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Collection<String> getFragments() {
		return fragments;
	}

	public void setFragments(Collection<String> fragments) {
		this.fragments = fragments;
	}

	public SearchHit getSearchHit() {
		return searchHit;
	}
	
	public void setSearchHit(SearchHit searchHit) {
		this.searchHit = searchHit;
	}

}
