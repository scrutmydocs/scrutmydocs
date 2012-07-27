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

package org.scrutmydocs.webapp.data;


import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.scrutmydocs.webapp.constant.SMDSearchProperties;
import org.scrutmydocs.webapp.helpers.FSRiverHelper;


public class Results {

	protected List<Result> results=new ArrayList<Result>();
	protected String took;
	protected boolean rendered = false;
	protected SearchResponse searchResponse;
	
	
	public Results(SearchResponse searchResponse) {
		this.searchResponse = searchResponse;
		
		this.took = searchResponse.getTook().format();

		for (SearchHit searchHit : searchResponse.getHits()) {
			Result result = new Result(searchHit);
//			searchHit.getHighlightFields();
//			searchHit.getId();
			
			if (searchHit.getSource() != null) {
				result.setTitle(FSRiverHelper.getSingleStringValue(SMDSearchProperties.DOC_FIELD_NAME, searchHit.getSource()));
				result.setContentType(FSRiverHelper.getSingleStringValue("file._content_type", searchHit.getSource()));
				result.setVirtualPath(FSRiverHelper.getSingleStringValue(SMDSearchProperties.DOC_FIELD_VIRTUAL_PATH, searchHit.getSource()));
			}

			if (searchHit.getHighlightFields() != null) {
				for (HighlightField highlightField : searchHit
						.getHighlightFields().values()) {

					String[] fragmentsBuilder = highlightField.getFragments();

					for (String fragment : fragmentsBuilder) {
						result.getFragments().add(fragment);
					}
				}
			}

			this.results.add(result);
		}
		
		this.rendered = !this.results.isEmpty();
	}

	public String getTook() {
		return took;
	}

	public void setTook(String took) {
		this.took = took;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}
	
	public boolean isRendered() {
		return rendered;
	}
	
	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}
	
	public SearchResponse getSearchResponse() {
		return searchResponse;
	}
	
	public void setSearchResponse(SearchResponse searchResponse) {
		this.searchResponse = searchResponse;
	}
}
