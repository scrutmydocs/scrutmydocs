package fr.issamax.essearch.data;

import java.util.ArrayList;
import java.util.Collection;

import org.elasticsearch.search.SearchHit;

public class Result {
	protected String title;
	protected Collection<String> fragments = new ArrayList<String>();
	protected SearchHit searchHit;
	protected String virtualPath;

	public Result(SearchHit searchHit) {
		this.searchHit = searchHit;
	}

	public Result(String title, String virtualPath) {
		this.title = title;
		this.virtualPath = virtualPath;
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

	public String getVirtualPath() {
		return virtualPath;
	}

	public void setVirtualPath(String virtualPath) {
		this.virtualPath = virtualPath;
	}
}
