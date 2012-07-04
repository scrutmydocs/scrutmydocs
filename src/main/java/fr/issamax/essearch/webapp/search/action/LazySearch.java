/*
 * Licensed to David Pilato and Malloum Laya (the "Authors") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Authors licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package fr.issamax.essearch.webapp.search.action;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.component.commandlink.CommandLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.data.Result;
import fr.issamax.essearch.data.Results;
import fr.issamax.essearch.service.search.SearchService;

/**
 * Dummy implementation of LazyDataModel that uses a list to mimic a real
 * datasource like a database.
 */
@Component
@Scope("request")
public class LazySearch implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7038031891023629994L;

	@Autowired
	protected SearchService searchService;

	protected String search;

	private Results results;

	private long totalPages;

	private int page;

	private boolean firstPage = true;

	private boolean lastPage = true;

	private boolean hasResults = false;

	public void init() {
		load(0, 10);
		page = 0;
	}

	public void next() {
		if (page + 1 < totalPages) {
			page++;
		} else {
			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage(null, new FacesMessage("WARNING",
					"this page is not available"));
		}

		load(page * 10, 10);

	}

	public void previous() {
		if (page != 0) {
			page--;
		} else {
			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage(null, new FacesMessage("WARNING",
					"this page is not available"));
		}

		load(page * 10, 10);
	}

	public List<Result> load(int first, int pageSize) {

		results = searchService.google(formatSearch(this.search), first, pageSize, null,
				null, null);

		this.setTotalPages(1 + results.getSearchResponse().getHits()
				.getTotalHits() / 10);

		if (results.getSearchResponse().getHits().getTotalHits() > 0) {
			hasResults = true;
		} else {
			hasResults = false;
		}
		if (page <= 0) {
			firstPage = true;
		} else {
			firstPage = false;
		}

		if (page + 1 >= totalPages) {
			lastPage = true;
		} else {
			lastPage = false;
		}

		CommandLink previous = (CommandLink) FacesContext.getCurrentInstance()
				.getViewRoot().findComponent(":formResult:previous");
		previous.setDisabled(firstPage);
		CommandLink next = (CommandLink) FacesContext.getCurrentInstance()
				.getViewRoot().findComponent(":formResult:next");
		next.setDisabled(lastPage);

		if (firstPage && lastPage) {
			previous.setRendered(false);
			next.setRendered(false);
		} else {
			previous.setRendered(true);
			next.setRendered(true);
		}

		return results.getResults();
	}
	
	
	protected String formatSearch(String mysearch){
		
		if(mysearch==null || mysearch.isEmpty() ) return null;
		mysearch = "*".concat(mysearch.toLowerCase()).concat("*");
		
		return mysearch;
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

	public long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public boolean isFirstPage() {
		return firstPage;
	}

	public void setFirstPage(boolean firstPage) {
		this.firstPage = firstPage;
	}

	public boolean isLastPage() {
		return lastPage;
	}

	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}

	public boolean isHasResults() {
		return hasResults;
	}

	public void setHasResults(boolean hasResults) {
		this.hasResults = hasResults;
	}

}
