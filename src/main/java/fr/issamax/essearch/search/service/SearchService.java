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

package fr.issamax.essearch.search.service;

import static fr.issamax.essearch.constant.ESSearchProperties.INDEX_NAME;
import static fr.issamax.essearch.constant.ESSearchProperties.INDEX_TYPE_DOC;
import static org.elasticsearch.index.query.FilterBuilders.prefixFilter;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryString;
import static org.elasticsearch.search.facet.FacetBuilders.termsFacet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.search.facet.terms.TermsFacet.Entry;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.data.Results;

@Component
public class SearchService {

	@Autowired
	protected Client esClient;

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	

	public Results google(String search,int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, String> filters) {
		if (logger.isDebugEnabled())
			logger.debug("google() : {}", search);

		Results results = null;
		try {
			QueryBuilder qb;
			if (search == null || search.trim().length() <= 0) {
				qb = matchAllQuery();
			} else {
				qb = queryString(search);
			}

			SearchResponse searchHits = esClient.prepareSearch()
					.setIndices(INDEX_NAME).setTypes(INDEX_TYPE_DOC)
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setQuery(qb).setFrom(first).setSize(pageSize)
					.addHighlightedField("name").addHighlightedField("file")
					.setHighlighterPreTags("<b>")
					.setHighlighterPostTags("</b>").execute().actionGet();

			results = new Results(searchHits);

		} catch (Exception e) {
			logger.error("/google() : {}", e.getMessage());
		}

		if (logger.isDebugEnabled())
			logger.debug("/google() : {}", search);

		return results;
		
	}

	/**
	 * Autocomplete TODO Create multifield mapping with edge n gram TODO Search
	 * and create facets
	 * 
	 * @param query
	 * @return
	 */
	public List<String> complete(String query) {
		List<String> results = new ArrayList<String>();

		try {
			QueryBuilder qb = matchAllQuery();
			FilterBuilder fb = prefixFilter("file", query);

			SearchResponse searchHits = esClient
					.prepareSearch()
					.setIndices(INDEX_NAME)
					.setTypes(INDEX_TYPE_DOC)
					.addFacet(
							termsFacet("autocomplete").field("file")
									.facetFilter(fb))

					.execute().actionGet();

			TermsFacet terms = searchHits.getFacets().facet("autocomplete");
			for (Entry entry : terms.entries()) {
				results.add(entry.getTerm());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

}
