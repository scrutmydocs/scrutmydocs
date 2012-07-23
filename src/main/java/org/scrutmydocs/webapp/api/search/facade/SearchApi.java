/*
 * Licensed to scrutmydocs.org (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
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

package org.scrutmydocs.webapp.api.search.facade;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scrutmydocs.webapp.api.common.RestAPIException;
import org.scrutmydocs.webapp.api.common.data.Api;
import org.scrutmydocs.webapp.api.common.facade.CommonBaseApi;
import org.scrutmydocs.webapp.api.search.data.RestResponseSearchResponse;
import org.scrutmydocs.webapp.api.search.data.SearchQuery;
import org.scrutmydocs.webapp.api.search.data.SearchResponse;
import org.scrutmydocs.webapp.service.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/search")
public class SearchApi extends CommonBaseApi {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	protected SearchService searchService;
	

	@Override
	public Api[] helpApiList() {
		Api[] apis = new Api[1];
		apis[0] = new Api("/search", "POST", "Search for documents");
		return apis;
	}
	
	@Override
	public String helpMessage() {
		return "The /search API helps you to search your documents.";
	}
	
	/**
	 * Search for documents
	 * @param query
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	RestResponseSearchResponse term(@RequestBody SearchQuery query) {
		SearchResponse results = null;
		try {
			results = searchService.google(query.getSearch(), query.getFirst(), query.getPageSize());
		} catch (Exception e) {
			return new RestResponseSearchResponse(new RestAPIException(e));
		}
		
		RestResponseSearchResponse response = new RestResponseSearchResponse(results);
		return response;
	}

	/**
	 * Search for documents
	 * @param term
	 * @return
	 */
	@RequestMapping(value = "{term}", method = RequestMethod.GET)
	public @ResponseBody RestResponseSearchResponse search(@PathVariable final String term) throws Exception {
		SearchResponse results = null;
		try {
			results = searchService.google(term, 0, 10);
		} catch (Exception e) {
			return new RestResponseSearchResponse(new RestAPIException(e));
		}
		
		RestResponseSearchResponse response = new RestResponseSearchResponse(results);
		return response;
	}
}
