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
