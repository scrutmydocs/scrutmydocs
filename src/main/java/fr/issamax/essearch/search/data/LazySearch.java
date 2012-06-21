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

package fr.issamax.essearch.search.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.data.Result;
import fr.issamax.essearch.data.Results;
import fr.issamax.essearch.search.service.SearchService;

/**
 * Dummy implementation of LazyDataModel that uses a list to mimic a real
 * datasource like a database.
 */
@Component
public class LazySearch extends LazyDataModel<Result> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7038031891023629994L;

	@Autowired
	protected SearchService searchService;

	protected String search;

	private Results results;

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
